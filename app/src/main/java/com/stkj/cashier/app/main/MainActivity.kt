package com.stkj.cashier.app.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.Display
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.util.valueIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.FutureTarget
import com.king.base.util.SystemUtils
import com.stkj.cashier.App
import com.stkj.cashier.R
import com.stkj.cashier.app.base.BaseActivity
import com.stkj.cashier.app.base.helper.SystemEventHelper
import com.stkj.cashier.app.base.helper.SystemEventHelper.OnSystemEventListener
import com.stkj.cashier.app.setting.Consumption1SettingFragment
import com.stkj.cashier.bean.CheckAppVersionBean
import com.stkj.cashier.bean.CompanyMemberBean
import com.stkj.cashier.bean.MessageEventBean
import com.stkj.cashier.bean.Result
import com.stkj.cashier.bean.db.CompanyMemberdbEntity
import com.stkj.cashier.cbgfacepass.CBGFacePassHandlerHelper
import com.stkj.cashier.cbgfacepass.data.CBGFacePassConfigMMKV
import com.stkj.cashier.cbgfacepass.permission.CBGPermissionRequest
import com.stkj.cashier.common.permissions.callback.PermissionCallback
import com.stkj.cashier.config.MessageEventType
import com.stkj.cashier.constants.Constants
import com.stkj.cashier.databinding.MainActivityBinding
import com.stkj.cashier.deviceinterface.DeviceManager
import com.stkj.cashier.dict.HomeMenu
import com.stkj.cashier.glide.GlideApp
import com.stkj.cashier.greendao.CompanyMemberdbEntityDao
import com.stkj.cashier.greendao.biz.CompanyMemberBiz
import com.stkj.cashier.greendao.tool.DBManager
import com.stkj.cashier.permission.AppPermissionHelper
import com.stkj.cashier.scan.ScanCodeCallback
import com.stkj.cashier.scan.SupportDevices
import com.stkj.cashier.scan.scan.ScanCallBack
import com.stkj.cashier.scan.scan.ScanTool
import com.stkj.cashier.util.ByteReverser
import com.stkj.cashier.util.ParseData
import com.stkj.cashier.util.QueueManager
import com.stkj.cashier.util.SerialNumber
import com.stkj.cashier.util.TimeUtils
import com.stkj.cashier.util.UpdateService
import com.stkj.cashier.util.keyevent.KeyEventResolver
import com.stkj.cashier.util.util.AppUtils
import com.stkj.cashier.util.util.BarUtils
import com.stkj.cashier.util.util.EncryptUtils
import com.stkj.cashier.util.util.LogUtils
import com.stkj.cashier.util.util.SPUtils
import com.stkj.cashier.util.util.ScreenUtils
import com.stkj.cashier.util.util.ToastUtils
import com.telpo.nfc.NfcUtil
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mcv.facepass.types.FacePassAddFaceResult
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets
import java.util.Arrays
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, MainActivityBinding>(), View.OnClickListener,ScanCallBack {

    
    val TAG = "MainActivity"
    var job: Job? = null
    private var mPresentation: DifferentDisplay? = null
    private lateinit var reportDeviceStatusDisposable: Disposable
    private val fragments by lazy {
        SparseArray<Fragment>()
    }
    var pageIndex = 1
    var pageSize = 5000
    private lateinit var mDisplayManager: DisplayManager//屏幕管理类

    private var currentTimeDisposable: Disposable? = null
    var queueManager = QueueManager()

    var callBack = false
    var beforeTimes  = 0L
    var face_0_Count = 0;
    var face_1_Count = 0;
    var face_2_Count = 0;
    var face_def_Count = 0;
    var totalFaceCount = 0;
    var beforeCardNumber = "";
    var mainFragment:MainFragment = MainFragment.newInstance();
    var consumption1SettingFragment:Consumption1SettingFragment = Consumption1SettingFragment.newInstance();

    var latch: CountDownLatch? = null

    //lateinit var progressDialog: AlertDialog //更新进度弹窗
    //lateinit var allFaceDownDialog: AlertDialog //全量人脸
    lateinit var tvProgress: TextView
    //lateinit var sbProgress: ProgressBar

    var allFaceDown: Boolean = false
    private var netStatusDisposable: Disposable? = null
    private var keyEventResolver: KeyEventResolver? = null

    private var isNetworkLost: Boolean = false

    private var scanCount = 0
    private var mHandlerThread: HandlerThread? = null
    private var mWorkHandler: Handler? = null
    //private var isFinish = true

    override fun onCreate(savedInstanceState: Bundle?) {
        try{
        // 检查首页Activity是否存在
        App.mMainActivity = this
        super.onCreate(savedInstanceState)

        initHandler();

        mainFragment.amountFragment.scanCodeCallback = object : ScanCodeCallback {
            override fun startScan() {
                handlePayLogic()
            }

            override fun stopScan() {
                GlobalScope.launch {
                    Log.d(TAG, "limescan stopScan == > 172")
//                    closeSerial()
//                    ScanTool.GET.release()
                }
            }

            override fun refund() {
                initMemberThread()
            }

        }

            GlobalScope.launch {
                if (!initScanTool()) {
                    ttsSpeak("该机型还没有适配扫码功能")
                } else {
                    ScanTool.GET.playSound(true)
                }
            }



    } catch (e: Throwable) {
        e.printStackTrace()
    }

    }

    private fun handlePayLogic() {
        if (job == null) {
        job = GlobalScope.launch {
            Log.d(TAG, "limescan startScan == > 162")
            initCardReader3()

//            if (!initScanTool()) {
//                ttsSpeak("该机型还没有适配扫码功能")
//            } else {
//                ScanTool.GET.playSound(true)
//            }

        }
    }
    }


    @SuppressLint("SetTextI18n")
    private fun initCardReader3() {
        //isFinish = false
        //tty580P
        NfcUtil.getInstance().initSerial("/dev/ttyHSL1", 9600)
        var receive: ByteArray? = null
        var startTime: Long? = null
        GlobalScope.launch {
            while (true) {
                Log.d(TAG, "initCardReader3: 寻卡")
                val selectCard = NfcUtil.getInstance().selectCard()
                if (selectCard != null) {
                    val cardType = selectCard.cardType
                    Log.e(TAG, "Card 类型:  == > " + selectCard.cardType)
                    Log.d(TAG, "卡号 : " + NfcUtil.toHexString(selectCard.cardNum))

                    if (cardType.toString() == "CPU") {
                        //发送apdu
                        startTime = System.currentTimeMillis()
                        receive = NfcUtil.getInstance().sendAPDU(NfcUtil.toBytes("00A4040009D15600002742444B01"), 1000)
                        if (receive != null) {
                            val toHexString = NfcUtil.toHexString(receive)
                            Log.d(TAG, "limecard APDU receive: $toHexString \n time: ${System.currentTimeMillis() - startTime!!}")
                            //sendMessage(0,toHexString)

                            return@launch
                        }
                    } else {
                        val copyOfRange = Arrays.copyOfRange(selectCard.cardNum, 0, 4)
                        Log.d(TAG, "limecard initCardReader3: $copyOfRange")
                        val idInfo = NfcUtil.toHexString(copyOfRange)
                        Log.d(TAG, "limecard receive 卡号 : $idInfo")
                        Log.d(TAG, "limecard receive  线程号 : ${android.os.Process.myTid()}")
                        //sendMessage(0,idInfo)
                        val card: String = ParseData.decodeHexStringIdcard2Int(ByteReverser.reverseHexBytes(idInfo))
                        Log.i(TAG,"limecard 读卡" + card)
                        Log.i(TAG,"limecard 读卡================================ " + DifferentDisplay.isStartFaceScan.get())
                        Log.d(TAG,"limecardcardNumber  CardReader: " + NfcUtil.toHexString(selectCard.cardNum).substring(0,8) + "   cardNumber: " + card)

                        if (DifferentDisplay.isStartFaceScan.get() && (mainFragment.amountFragment.isPaying() || mainFragment.amountFragment.isRefund())) {
                            DifferentDisplay.isStartFaceScan.set(false)
                            var currentTimes  = System.currentTimeMillis()

                            if (((currentTimes - beforeTimes) <= 1000) && beforeCardNumber.equals(card)){
                                //return@launch
                                continue
                            }

                            if ((currentTimes - beforeTimes) < 500){
                                //return@launch
                                continue
                            }else{
                                beforeTimes = currentTimes
                            }


                            beforeCardNumber = card
                            EventBus.getDefault().post(
                                MessageEventBean(
                                    MessageEventType.AmountCard,
                                    card
                                )
                            )
                        }
                    }


                }

                Log.d(TAG, "limeinitCardReader3:   " + System.currentTimeMillis())

            }
        }
    }


    private fun openSerial() {
//        isFinish = false
        //tty580P
        NfcUtil.getInstance().initSerial("/dev/ttyHSL1", 9600)
    }

    private fun closeSerial(){
        //isFinish = true
        NfcUtil.getInstance().destroySerial()
    }



    private fun initHandler() {
        mHandlerThread = HandlerThread("wt")
        mHandlerThread!!.start()
        mWorkHandler = object : Handler(mHandlerThread!!.getLooper()) {
            var writingFile: Boolean = false
            var mFile: File? = null
            var mFos: FileOutputStream? = null

            override fun handleMessage(@NonNull msg: Message) {
                super.handleMessage(msg)
                GlobalScope.launch {
                val data: String = msg.obj.toString()
                if (!writingFile) {
                    val filePath =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "ScanCodeContext.txt"
                    mFile = File(filePath)
                    if (mFile!!.exists()) {
                        mFile!!.delete()
                    }
                    try {
                        mFile!!.createNewFile()
                        mFos = FileOutputStream(mFile)
                        mFos!!.write(data.toByteArray(StandardCharsets.UTF_8))
                        writingFile = true
                    } catch (pE: IOException) {
                        pE.printStackTrace()
                    }
                } else {
                    try {
                        mFos!!.write(data.toByteArray(StandardCharsets.UTF_8))
                    } catch (pE: IOException) {
                        pE.printStackTrace()
                    }
                }

            }

            }
        }
    }

    /**
     * 判断使用模式
     *
     * @return 返回true表示表示该机型已经适配，false表示该机型还没有适配
     */
    private fun initScanTool(): Boolean {

        for (s in SupportDevices.sDevices.keys) {
            if (s == Build.MODEL) {
                val pair = SupportDevices.sDevices[s] ?: continue
                Log.d(TAG, "limescan judgeModel == > $s")
                Log.d(TAG, "limescan path == > " + pair.first)
                Log.d(TAG, "limescan baud rate == > " + pair.second)
                ScanTool.GET.initSerial(this, pair.first, pair.second)
                ScanTool.GET.setScanCallBack(this@MainActivity)
                return true
            }
        }

        return false
    }


    override fun onScanCallBack(data: String?) {
        if (TextUtils.isEmpty(data)) return
        Log.d(TAG, ("limescan  回调数据 == > $data").toString())

        Log.e(TAG,"limeparams 247: onScanCallBack ========================================")

        if(data?.length!! > 8){

            if (mainFragment.amountFragment.isPaying() || mainFragment.amountFragment.isRefund()) {

                var currentTimes  = System.currentTimeMillis()

                Log.d(TAG, "limescan  回调数据 == > " + 386)
                if (((currentTimes - beforeTimes) <= 1000) && beforeCardNumber.equals(data)){
                    //return@launch
                    return
                }
                Log.d(TAG, "limescan  回调数据 == > " + 391)
                if ((currentTimes - beforeTimes) < 500){
                    //return@launch
                    return
                }else{
                    beforeTimes = currentTimes
                }
                beforeCardNumber = data
                Log.i(TAG, "limescan  回调数据 == > " + 399)
                EventBus.getDefault().post(
                    MessageEventBean(
                        MessageEventType.AmountScanCode,
                        data
                    )
                )
            }
        }



//        scanCount++
//        //将内容写入文件，用作测试验证
//        val message = mWorkHandler!!.obtainMessage()
//        message.obj = ("次数：" + String.format(
//            Locale.getDefault(),
//            "%05d",
//            scanCount
//        ) + "：" + data).toString() + "\n"
//        mWorkHandler!!.sendMessage(message)
    }

    override fun onInitScan(isSuccess: Boolean) {
        super.onInitScan(isSuccess)
        Log.d(TAG, "limescan onInitScan == > $isSuccess")
        val str = if (isSuccess) "初始化成功" else "初始化失败"
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        ScanTool.GET.playSound(true)
    }


    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        try{
        LogUtils.e("主页initData")
        //RkSysTool.getInstance().initContext(this)
        //RkSysTool.getInstance().setStatusBar(false)
       // RkSysTool.getInstance().setNavitionBar(false)

        SPUtils.getInstance().put(Constants.SWITCH_CONSUMPTION_DIALOG, 0) //不显示消费确认框
        showFragment(HomeMenu.MENU1)
        SPUtils.getInstance().put(Constants.FRAGMENT_SET, false)

        ScreenUtils.setFullScreen(this)
        BarUtils.setNavBarVisibility(this, false)
        EventBus.getDefault().register(this)
//        initUsbCard()
//        viewDataBinding.ivWifiState.setOnClickListener(this)
        App.serialNumber = SerialNumber.getMachineNumber()
        LogUtils.e("serialNumber", App.serialNumber)
        //binding.tvSerialNumber.text = App.serialNumber
        clickVersionUpdate()
        showDifferentDisplay()

        initCheckStatus()
        getIntervalCardType()
        Handler().postDelayed({
            company()
            }, 1500)
        viewModel.deviceStatus.observe(this) {
            LogUtils.e("deviceStatus observe")
            var indexs = it.data?.updateUserInfo?.split("&")
            if (indexs != null) {
                for (item in indexs) {
                    LogUtils.e("心跳item" + item + "/" + callBack + "/" + latch?.count + "/" + allFaceDown)
                    if (item == "1") {
                        /* if (queueManager == null) {
                             LogUtils.e("queueManager=null")
                             queueManager = QueueManager()
                         }
                         if (!callBack&&(latch?.count==0L||latch==null)&&!allFaceDown) {
                             queueManager.addTask(Runnable {
                                 LogUtils.e("队列新增")
                                 companyMember(1)
                             })
                             LogUtils.e("item下发" + item)
                         }*/
                        if ((latch?.count == 0L || latch == null) && !allFaceDown) {
                            companyMember(1)
                            LogUtils.e("item下发" + item)
                        }
//                        isBreak=true
//
                    } else if (item == "2") {
                        getIntervalCardType()
                    } else if (item == "3") {
                        offlineSet()
                    } else if (item == "4") {
                        company()
                    }
                }
            }

        }

        viewModel.offlineSet.observe(this) {
            LogUtils.e("offlineSet observe")
        }
        viewModel.company.observe(this) {
            Log.d(TAG,"limecompany observe it.data?.deviceName " + it.data?.deviceName  + "  it.company " + it.company)
            //{"Code":10000,"company":"测试服","Data":{"deviceName":"键盘设备"},"Message":"成功"}

//            binding.tvCompanyName.text = it.company
            EventBus.getDefault().post(
                MessageEventBean(
                    MessageEventType.CompanyName,
                    it.company,
                    it.data?.deviceName
                )
            )
        }
        viewModel.companyMember.observe(this) { items ->
            LogUtils.e("lime== 人脸录入后台返回的人数" + (items.data?.results?.size))
            if (items.code == 10000) {
                LogUtils.e("companyMember", "==" + items.data?.results?.size)
//            checkFace(it.results)
                CompanyMemberBiz.addCompanyMembers(items.data?.results)
//            CompanyMemberBiz.getCompanyMemberList {
//                if (it.size == items.totalCount){
//                callBack = true

                //latch = items.data?.results?.size?.let { CountDownLatch(it) }

                checkFace(items.data?.results)
                EventBus.getDefault().post(MessageEventBean(MessageEventType.FaceDBChange))

                //latch?.await(); // 等待所有请求完成
                //LogUtils.e("人脸录入调用companyMember方法2")
                //companyMember(1)

                /*Observable.timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { aLong ->
                        LogUtils.e("人脸录入调用companyMember方法2")
                        companyMember(1)
                    }*/
            } else {
                if (items.code == 10024) {
                    callBack = false
                    allFaceDown = false
//                    if (allFaceDownDialog != null) {
//                        allFaceDownDialog.dismiss()
//                    }
                    queueManager.clearTasks()
                    EventBus.getDefault().post(MessageEventBean(MessageEventType.FaceDBChangeEnd))


                } else {
                    items.message?.let { it1 -> showToast(it1) }
                }
            }
//                }
//            }
        }
        viewModel.setCallback(object : MainViewModel.MyCompanyMemberCallback {
            override fun onDataReceived(items: Result<CompanyMemberBean>) {
                LogUtils.e("lime== 人脸录入后台返回的人数Callback" + (items.data?.results?.size))
                LogUtils.e("lime== items.code " + (items.code))
                // 10000 人脸数据下发成功
                if (items.code == 10000) {
                    LogUtils.e("lime== companyMember", "==" + items.data?.results?.size)
                    CompanyMemberBiz.addCompanyMembers(items.data?.results)

                    checkFace(items.data?.results)
                    EventBus.getDefault().post(MessageEventBean(MessageEventType.FaceDBChange))
                    //latch?.await(); // 等待所有请求完成
                    //LogUtils.e("人脸录入调用companyMember方法2")
                    //companyMember(1)
                    /*Observable.timer(3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { aLong ->
                            LogUtils.e("人脸录入调用companyMember方法2")
                            companyMember(1)
                        }*/
                } else {
                    //10024 人脸数据下发结束
                    if (items.code == 10024) {
                        callBack = false
                        queueManager.clearTasks()
                        EventBus.getDefault()
                            .post(MessageEventBean(MessageEventType.FaceDBChangeEnd))
                        allFaceDown = false
                        //isBreak=false
                        //isDownFace=false
//                        if (allFaceDownDialog != null) {
//                            allFaceDownDialog.dismiss()
//                        }

                        //显示人脸数
//                        var faceTokens: Array<ByteArray>? = null
//                        if (App.mFacePassHandler != null) {
//                            faceTokens =
//                                App.mFacePassHandler!!.getLocalGroupInfo(Constants.GROUP_NAME)
//                        }
//                        CompanyMemberBiz.getCompanyMemberList {
//                            LogUtils.e("getCompanyMemberList", "==" + it.size)
//                            binding.tvCheckMember.text =
//                                "已入库(" + faceTokens?.size + "/" + it.size + ")"
//                        }

                    } else {
                        items.message?.let { it1 -> showToast(it1) }
                    }
                }
            }

            override fun onError(error: Throwable) {
                cancelAllFaceDownDialog()
            }

        })
        viewModel.companyMemberStatus.observe(this) {
            // 处理响应
            latch?.countDown(); // 每个请求完成后递减计数器
            LogUtils.e("callBack计数器", "==$callBack" + latch?.count)
            LogUtils.e("companyMemberStatus observe")
//            SPStaticUtils.put(SPKey.KEY_CONFIG,it.getAiStatus())
        }
        viewModel.intervalCardType.observe(this) {
            LogUtils.e("intervalCardType observe")
//            SPStaticUtils.put(SPKey.KEY_CONFIG,it.getAiStatus())
            App.intervalCardType = it
            // lime modify begin
            requestConsumptionType()
            //checkCurrentAmountMode()
            // lime modify end
            Handler().postDelayed({
                EventBus.getDefault()
                    .post(MessageEventBean(MessageEventType.IntervalCardType))
            }, 1500)


        }
        viewModel.checkAppVersion.observe(this) {
            Log.d(TAG,"limecheckAppVersion 596")
            if (it.code == 10000) {
                if (it.data?.version?.toInt()!! > AppUtils.getAppVersionCode()) {
                    // showUpdataDialog(it.data)
//                    mPresentation?.dismiss()
//                    showUpdateDisplay()
                    showUpdataProgressDialog(it.data)


                }
            } else {
                it.message?.let { it1 -> showToast(it1) }
            }
        }
        viewModel.equUpgCallback.observe(this) {
            LogUtils.e("equUpgCallback observe")
        }
        viewModel.currentTimeInfo.observe(this) {
            LogUtils.e("currentTimeInfo observe")
            if (it.code == 10000) {
                App.currentTimeInfo = it.data
                EventBus.getDefault().post(MessageEventBean(MessageEventType.CurrentTimeInfo))
            } else {
                EventBus.getDefault().post(MessageEventBean(MessageEventType.CurrentTimeInfoFail))
            }
        }
//        var faceTokens: Array<ByteArray>? = null
//        if (App.mFacePassHandler != null) {
//            faceTokens = App.mFacePassHandler!!.getLocalGroupInfo(Constants.GROUP_NAME)
//        }
//        val faceTokenList: MutableList<String> = ArrayList()
//        if (faceTokens?.isNotEmpty() == true) {
//            for (j in faceTokens?.indices!!) {
//                if (faceTokens!![j].isNotEmpty()) {
//                    faceTokenList.add(String(faceTokens!![j]))
//                }
//            }
//            CompanyMemberBiz.getCompanyMemberList {
//                LogUtils.e("getCompanyMemberList", "==" + it.size)
//                binding.tvCheckMember.text = "已入库(" + faceTokens!!.size + "/" + it.size + ")"
//            }
//        }
//
        initMemberThread()

        //网络状态
        requestNetStatus()
        viewModel.netStatus.observe(this) {
            // LogUtils.d("网络状态" + it)
            if (viewModel.isSuccess(it)) {
                if (isNetworkLost) {
                    Log.i("viewModel.netStatus", "net connect success")
                    isNetworkLost = false
//                    ttsSpeak("网络连接成功")
                    //请求餐厅时段信息
                    getIntervalCardType()
                }
            } else {
                if (!isNetworkLost) {
                    Log.i("viewModel.netStatus", "isNetworkLost")
                    isNetworkLost = true
//                    ttsSpeak("网络已断开")
                }
            }
        }
        setCallback(object : MyCallback {
            override fun onDataReceived(data: String) {
                LogUtils.e("网络消息回调2" + data)
                // ToastUtils.showShort("断网")
                when (data) {
                    getString(R.string.result_network_unavailable_error),
                    getString(R.string.result_connect_timeout_error),
                    getString(R.string.result_connect_failed_error) -> {

                        cancelAllFaceDownDialog()

                    }

                    else -> LogUtils.e("网络消息回调" + data)
                }
            }

            override fun onError(error: Exception) {
            }
        })

//        val builder = AlertDialog.Builder(this, R.style.app_dialog)
//        allFaceDownDialog = builder.create()
//        allFaceDownDialog.setCancelable(false)

        keyEventResolver = KeyEventResolver(object : KeyEventResolver.OnScanSuccessListener {
            override fun onScanSuccess(barcode: String?) {
                LogUtils.e("扫码: 键盘" + barcode)

                Log.e(TAG,"limeparams 571: onScanSuccess ========================================")
//                if(barcode?.length!! > 8) {
//                    EventBus.getDefault().post(
//                        MessageEventBean(
//                            MessageEventType.AmountScanCode,
//                            barcode
//                        )
//                    )
//
//                }
            }
        })

        //系统事件监听
        SystemEventHelper.INSTANCE.addSystemEventListener(systemEventListener)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    }

    private fun cancelAllFaceDownDialog() {
//        if (allFaceDownDialog.isShowing || allFaceDown) {
//            allFaceDownDialog.dismiss()
//            EventBus.getDefault()
//                .post(MessageEventBean(MessageEventType.FaceDBChangeEnd))
//            allFaceDown = false
//
//            latch = CountDownLatch(1) // 新的latch，初始计数为5
//            latch?.countDown()
//        }
    }

    private fun initMemberThread() {
//        Thread(Runnable {
//            while (true) {
//                if (queueManager.hasMoreTasks() && !callBack) {
//                    callBack = true
//                    queueManager.nextTask?.run()
//                    LogUtils.e("队列执行")
//                }
//            }
//        }).start()

        GlobalScope.launch {
            //while (true) {
                if (queueManager.hasMoreTasks() && !callBack) {
                    callBack = true
                    queueManager.nextTask?.run()
                    LogUtils.e("队列执行")
                }
            //}
        }

    }

    @SuppressLint("AutoDispose")
    private fun initCheckStatus() {
        var time = SPUtils.getInstance().getInt(Constants.FACE_HEAD_BEAT, 30).toLong()
        reportDeviceStatusDisposable = Observable.interval(0, time, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aLong ->
                // 点击回复按钮
                var map = hashMapOf<String, Any>()
                map["mode"] = "ReportDeviceStatus"
                map["machine_Number"] = App.serialNumber
                var md5 = EncryptUtils.encryptMD5ToString16(App.serialNumber)
                map["sign"] = md5
                viewModel.reportDeviceStatus(map)


            }
    }

    @SuppressLint("AutoDispose")
    private fun requestConsumptionType() {
        currentTimeDisposable?.dispose()
        currentTimeDisposable = Observable.interval(0, 60, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aLong ->
                //一分钟检查金额模式 固定模式
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    checkCurrentAmountMode()
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkCurrentAmountMode() {
        LogUtils.e("checkCurrentAmountMode start")
        val switchFixAmount = SPUtils.getInstance().getBoolean(Constants.SWITCH_FIX_AMOUNT, false);
        if (switchFixAmount) {
            val currentFixAmountTime =
                SPUtils.getInstance().getString(Constants.CURRENT_FIX_AMOUNT_TIME, "")
            if (!TextUtils.isEmpty(currentFixAmountTime)) {
                val split = currentFixAmountTime.split("-")
                if (split.size >= 2) {
                    LogUtils.e("checkCurrentAmountMode RefreshFixAmountMode")
                    if (!TimeUtils.isCurrentTimeIsInRound(split[0], split[1])) {
                        Log.i(TAG, "limeRefund ========> MessageEventType.RefreshFixAmountMode 757")
                        EventBus.getDefault()
                            .post(MessageEventBean(MessageEventType.RefreshFixAmountMode))
                    }
                }
            } else {
                LogUtils.e("checkCurrentAmountMode RefreshFixAmountMode empty currentFixAmountTime")
                Log.i(TAG, "limeRefund ========> MessageEventType.RefreshFixAmountMode 764")
                EventBus.getDefault().post(MessageEventBean(MessageEventType.RefreshFixAmountMode))
            }
        }
        LogUtils.e("checkCurrentAmountMode end")
    }

    @SuppressLint("AutoDispose")
    private fun requestNetStatus() {
        netStatusDisposable?.dispose()
        netStatusDisposable = Observable.interval(0, 10, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aLong ->
                var timeMap = hashMapOf<String, Any>()
                timeMap["mode"] = "HealthCheck"
                timeMap["machine_Number"] = App.serialNumber
                var timeMd5 = EncryptUtils.encryptMD5ToString16(App.serialNumber)
                timeMap["sign"] = timeMd5
                viewModel.networkStatus(timeMap, errorCallback = {
                    when (it) {
                        is SocketTimeoutException -> if (!isNetworkLost) {
                            isNetworkLost = true
//                            ttsSpeak("网络已断开")
                            Log.i("viewModel.netStatus", "isNetworkLost")
                        }

                        is ConnectException -> if (!isNetworkLost) {
                            isNetworkLost = true
//                            ttsSpeak("网络已断开")
                            Log.i("viewModel.netStatus", "isNetworkLost")
                        }

                        is HttpException -> if (!isNetworkLost) {
                            isNetworkLost = true
//                            ttsSpeak("网络异常")
                            Log.i("viewModel.netStatus", "isNetworkLost")
                        }
                    }
                })
            }
    }


    private fun initUsbCard() {
              try {
                  //  /dev/ttyHSL1 9600 读卡  /dev/ttyHSL0 115200 扫码
                  //NfcUtil.getInstance().initSerial("/dev/ttyHSL1", 9600)
                  job =  GlobalScope.launch {
                      while (isFinishing.not()) {
                          NfcUtil.getInstance().initSerial("/dev/ttyHSL1", 9600)
                          val selectCard = NfcUtil.getInstance().selectCard()
                          if (selectCard != null) {
                              Log.d(TAG, "limecard handleMessage:  == > " + selectCard.cardType)

                              Log.d(TAG, "limecard initCardReader: " + NfcUtil.toHexString(selectCard.cardNum).substring(0,8))

                              var cardHex: String = ""
                              if (NfcUtil.toHexString(selectCard.cardNum).length >= 8){
                                  cardHex = NfcUtil.toHexString(selectCard.cardNum).substring(0,8)
                              }else{
                                  //continue
                              }

                              //Log.d(TAG, "limecard *******************************************selectCard.cardNum: \n" + selectCard.cardNum)
                              val card: String = ParseData.decodeHexStringIdcard2Int(ByteReverser.reverseHexBytes(NfcUtil.toHexString(selectCard.cardNum).substring(0,8)))
                              Log.i(TAG,"limecard 读卡" + card)
                              Log.i(TAG,"limecard 读卡================================ " + DifferentDisplay.isStartFaceScan.get())
                              Log.d(TAG,"limecardcardNumber  CardReader: " + NfcUtil.toHexString(selectCard.cardNum).substring(0,8) + "   cardNumber: " + card)

                              if (DifferentDisplay.isStartFaceScan.get() && (mainFragment.amountFragment.isPaying() || mainFragment.amountFragment.isRefund())) {
                                  DifferentDisplay.isStartFaceScan.set(false)
                                  EventBus.getDefault().post(
                                      MessageEventBean(
                                          MessageEventType.AmountCard,
                                          card
                                      )
                                  )
                              }


                          }
                          delay(300)
                          Log.i(TAG,"meme launch job?.isActive " + job?.isActive)
                      }
                  }

              } catch (e: Throwable) {
                  Log.i(TAG,"meme launch Throwable " + e.message)
              }
    }


    private fun checkFace(entities: List<CompanyMemberdbEntity>?) {
        Thread(Runnable {
//            CompanyMemberBiz.getCompanyMemberList {
            face_0_Count = 0;
            face_1_Count = 0;
            face_2_Count = 0;
            face_def_Count = 0;
            totalFaceCount = 0;

            latch = entities?.size?.let { CountDownLatch(it) }

            for ((index, item) in entities?.withIndex()!!) {
                LogUtils.e("lime*** 下发人脸中 589")
                val unique =
                    DBManager.getInstance().daoSession.companyMemberdbEntityDao.queryBuilder()
                        .where(CompanyMemberdbEntityDao.Properties.UniqueNumber.eq(entities[index].uniqueNumber))
                        .unique()
                LogUtils.e("lime*** 下发人脸中 594")
                //LogUtils.e("lime== 人脸add" + unique.cardState + "//"+unique?.faceToken )
                LogUtils.e("lime*** 下发人脸中 596")
                if (unique != null && unique.cardState != 64 && unique?.faceToken == null) {
//                    if (entities[index].cardState != 64) {
//                        entities[i].id = unique.id
//                        DBManager.getInstance().daoSession.companyMemberdbEntityDao.update(
//                            entities[i]
//                        )
//                    }
                    LogUtils.e("lime*** 下发人脸中 603")
                    var base64ToBitmap: Bitmap? = null
                    try {
                        LogUtils.e("lime*** 下发人脸中 606")
                        val futureTarget: FutureTarget<Bitmap> =
                            GlideApp.with(App.applicationContext)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .load(item.imgData)
                                .submit()
                        base64ToBitmap = futureTarget.get()
                    } catch (e: Throwable) {
                        LogUtils.e(
                            "-facePassHelper-addFacePassToLocal--cardNumber: " + item.cardNumber
                                    + " load imageData error " + item.imgData
                        )
                        e.printStackTrace()
                    }
                    // var base64ToBitmap = ImageUtils.base64ToBitmap(item.imgData)
                    try {
                        if (base64ToBitmap == null) {
                            item.result = -99
                            LogUtils.e("lime== 人脸token生成失败" + item.fullName + "///下载图片失败")
                            CompanyMemberBiz.updateCompanyMember(item)
                        } else {
                            val result: FacePassAddFaceResult? =
                                App.mFacePassHandler?.addFace(base64ToBitmap)
                            totalFaceCount += 1;
                            if (result?.result == 0) {
                                //0：成功
                                item.result = 20
                                face_0_Count += 1;

                            } else if (result?.result == 1) {
                                //1：没有检测到人脸
                                face_1_Count += 1;
                                item.result = 3
                                LogUtils.e("lime== 入库失败--没有检测到人脸" + item.fullName)
                            } else if (result?.result == 2) {
                                //2：检测到人脸，但是没有通过质量判断
                                face_2_Count += 1;
                                item.result = 4
                                LogUtils.e("lime== 入库失败--检测到人脸，但是没有通过质量判断" + item.fullName)
                            } else {
                                //其他值：未知错误
                                face_def_Count += 1;
                                item.result = 5
                                LogUtils.e("lime== 入库失败--其他值：未知错误" + item.fullName)
                            }
                            if (result?.result == 0) {
                                item.faceToken = String(result.faceToken, charset("ISO-8859-1"))
//                            unique.faceToken = String(result.faceToken)
                                LogUtils.e("lime== 人脸token生成成功" + item.fullName + item.phone + index + "///" + item.faceToken)
                                CompanyMemberBiz.updateCompanyMember(item)
                                try {
                                    val b: Boolean = App.mFacePassHandler!!.bindGroup(
                                        Constants.GROUP_NAME,
                                        result.faceToken
                                    )
                                    val result = if (b) "success " else "failed"
//                                toast("bind  $result")
                                    LogUtils.e("addFace", "bind  $result")
                                } catch (e: Exception) {
                                    e.printStackTrace()
//                                toast(e.message)
                                }
                            } else {
                                LogUtils.e("lime== 人脸token生成失败" + item.fullName + "///" + item.faceToken)
                                CompanyMemberBiz.updateCompanyMember(item)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        LogUtils.e("lime== addFace", item.fullName + e.message)
                        // toast(e.message)
                    } finally {
                        //base64ToBitmap?.recycle()


                    }

                }

                LogUtils.e("lime*** 下发人脸中 682")

                downFaceFail(item, 0)
                LogUtils.e(
                    "人脸",
                    "lime== ==$callBack" + callBack + item.fullName + item.phone + "imgData " + item.imgData + "index" + index + "/" + (entities.size - 1)
                )
                // 处理响应
                //latch?.countDown(); // 每个请求完成后递减计数器

                if (index == entities.size - 1) {
                    // Thread.sleep(1000)
                    callBack = false
//                    isDownFace = false

                    LogUtils.e("lime== callBack", "==$callBack" + latch?.count)

                }
            }

            LogUtils.w("lime============ face_0_Count: " + face_0_Count)
            LogUtils.w("lime============ face_1_Count: " + face_1_Count)
            LogUtils.w("lime============ face_2_Count: " + face_2_Count)
            LogUtils.w("lime============ face_def_Count: " + face_def_Count)
            LogUtils.w("lime============ totalFaceCount: " + totalFaceCount)


//            LogUtils.w(
//                "lime============  sdk Face Count face_pass_1: " + App?.mFacePassHandler?.getLocalDyGroupFaceNum(
//                    "face_pass_1"
//                )
//            )


            //   companyMember()
            latch?.await(); // 等待所有请求完成
            //LogUtils.e("人脸录入调用companyMember方法2")
            companyMember(1)
//            }
        }).start()


    }

    private fun initApp() {
        // 初始化人脸识别
        val facePassHelper = getWeakRefHolder(CBGFacePassHandlerHelper::class.java)
        facePassHelper?.setOnInitFacePassListener(object : CBGFacePassHandlerHelper.OnInitFacePassListener {
            override fun onInitSuccess() {
                //initData()
            }

            override fun onInitError(msg: String) {
                //hideLoadingDialog()
//                CommonDialogUtils.showTipsDialog(this@MainActivity, msg, "知道了") { alertDialogFragment ->
//                    initData()
//                }

                ToastUtils.showLong(msg)

            }
        })
        AppPermissionHelper.with(this)
            .requestPermission(CBGPermissionRequest(), object : PermissionCallback {
                override fun onGranted() {
                    showLoadingDialog()
                    // 设备识别距离阈值
                    val defaultFaceMinThreshold = DeviceManager.getInstance().defaultDetectFaceMinThreshold
                    CBGFacePassConfigMMKV.setDefDetectFaceMinThreshold(defaultFaceMinThreshold)
                    // 设备人脸入库阈值
                    val defaultAddFaceMinThreshold = DeviceManager.getInstance().defaultAddFaceMinThreshold
                    CBGFacePassConfigMMKV.setDefAddFaceMinThreshold(defaultAddFaceMinThreshold)
                    // 设备人脸角度阈值
                    val defaultPoseThreshold = DeviceManager.getInstance().defaultPoseThreshold
                    CBGFacePassConfigMMKV.setDefPoseThreshold(defaultPoseThreshold)
                    val supportDualCamera = DeviceManager.getInstance().isSupportDualCamera
                    val facePassConfig = CBGFacePassConfigMMKV.getFacePassConfig(supportDualCamera)
                    val facePassHelper = getWeakRefHolder(CBGFacePassHandlerHelper::class.java)
                    facePassHelper?.initAndAuthSdk(facePassConfig)
                }

                override fun onCancel() {
//                    CommonDialogUtils.showTipsDialog(this@MainActivity, "人脸识别功能请求系统权限失败", "知道了") { alertDialogFragment ->
//                        initData()
//                    }
                    ToastUtils.showLong("人脸识别功能请求系统权限失败")

                }
            })
    }




    private fun clickVersionUpdate() {
        // TODO 处理点击“版本更新”逻辑

        var map = hashMapOf<String, Any>()
        map["mode"] = "CheckAppVersion"
        map["machine_Number"] = App.serialNumber
//        map["deviceType"] = "1"
        map["deviceType"] = getString(R.string.deviceType)//设备厂商+产品+设备型号
        map["version_No"] = AppUtils.getAppVersionCode()
        // var md5 =EncryptUtils.encryptMD5ToString16("1&" + App.serialNumber + "&" + AppUtils.getAppVersionCode())
        var md5 =
            EncryptUtils.encryptMD5ToString16(getString(R.string.deviceType) + "&" + App.serialNumber + "&" + AppUtils.getAppVersionCode())
        map["sign"] = md5
        viewModel.checkAppVersion(map)
    }

    fun ttsSpeak(value: String) {
        try{
        App.TTS.setSpeechRate(1f)
        App.TTS.speak(
            value,
            TextToSpeech.QUEUE_FLUSH, null
        )
        }catch (e:Exception){

        }
    }

    private fun company() {
        var companyMap = hashMapOf<String, Any>()
        companyMap["mode"] = "company_setup"
        companyMap["machine_Number"] = App.serialNumber
        var companyMd5 = EncryptUtils.encryptMD5ToString16(App.serialNumber)
        companyMap["sign"] = companyMd5
        viewModel.companySetup(companyMap)
    }

    private fun offlineSet() {
        var map = hashMapOf<String, Any>()
        map["mode"] = "OfflineSet"
        map["machine_Number"] = App.serialNumber
        var md5 = EncryptUtils.encryptMD5ToString16(App.serialNumber)
        map["sign"] = md5
        viewModel.offlineSet(map)
    }

    private fun getIntervalCardType() {
        val map: MutableMap<String, Any> = HashMap()
        var deviceId = App.serialNumber
        map["mode"] = "GetIntervalCardType"
        map["machine_Number"] = deviceId
        var md5 = EncryptUtils.encryptMD5ToString16(deviceId)
        map["sign"] = md5
        viewModel.getIntervalCardType(map);

    }

    fun companyMember(inferior_type: Int) {
        LogUtils.e("lime== 人脸录入companyMember" + inferior_type)
        if (inferior_type == 0) {
            if (SystemUtils.isNetWorkActive(getApp())) {
                runOnUiThread {
                    showAllFsceProgressDialog()
                }
            } else {
                //ttsSpeak(getString(R.string.result_network_unavailable_error))
//                ttsSpeak("网络已断开，请检查网络。")
                EventBus.getDefault().post(MessageEventBean(MessageEventType.FaceDBChangeEnd))
                allFaceDown = false
                return
            }
        }
        allFaceDown = true
        /*  Thread(Runnable {
              try {
                  // 在子线程中执行的代码
                  // 输出日志信息
                  LogUtils.d("MyThread", "子线程开始执行isBreak" + isBreak + "isDownFace" + isDownFace)

                  while (isBreak) {
                      if (!isDownFace) {
                          isDownFace = true
                      }
                  }
                  // 再次输出日志信息
                  LogUtils.d("MyThread", "子线程执行完毕")
              } catch (e: InterruptedException) {
                  // 处理中断异常
                  e.printStackTrace()
                  LogUtils.e("MyThread", "子线程被中断", e)
              }
              //  LogUtils.e("人脸录入companyMember" + gett)
          }).start()*/

        var companyMap = hashMapOf<kotlin.String, Any>()
        //companyMap["mode"] = "CompanyMember"
        companyMap["mode"] = "KeyBoardCompanyMember" //键盘设备录入人员

        companyMap["inferior_type"] = inferior_type
        companyMap["machine_Number"] = App.serialNumber
        companyMap["pageIndex"] = pageIndex
        companyMap["pageSize"] = pageSize
        var companyMd5 =
            EncryptUtils.encryptMD5ToString16("$inferior_type&" + App.serialNumber + "&$pageIndex&$pageSize")
        companyMap["sign"] = companyMd5
        viewModel.companyMember(companyMap)
    }

    private fun downFaceFail(item: CompanyMemberdbEntity, isFinish: Int) {
        var companyMap = hashMapOf<String, Any>()
        companyMap["mode"] = "DownFaceFail"
//        companyMap["cardNumber"] = item.cardNumber
        companyMap["customerId"] = item.uniqueNumber
        if (item.result == null) {
            item.result = 1
            companyMap["errorType"] = 1
        } else {
            companyMap["errorType"] = item.result
        }

        companyMap["isFinish"] = isFinish
        companyMap["machine_Number"] = App.serialNumber

        // var companyMd5 =EncryptUtils.encryptMD5ToString16(item.cardNumber + "&" + item.result + "&$isFinish&" + App.serialNumber)
        var companyMd5 =
            EncryptUtils.encryptMD5ToString16(item.uniqueNumber + "&" + item.result + "&$isFinish&" + App.serialNumber)
        companyMap["sign"] = companyMd5
        viewModel.downFaceFail(companyMap)

    }

    override fun getLayoutId(): Int {
        return R.layout.main_activity
    }

    //双屏显示
    private fun showDifferentDisplay() {
        mDisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager;
        //得到显示器数组
        var displays = mDisplayManager.displays
        Log.d(TAG,"limescreen 1177 屏数量" + displays.size)

        if (displays.size > 1) {
            try {
                if (mPresentation == null) {
                    Log.d(TAG,"limescreen 1182 副屏1")
                    mPresentation = DifferentDisplay(this, displays[0])
                    mPresentation?.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                     Handler().postDelayed({
                        Log.d(TAG,"limescreen 1190 副屏3")
                        mPresentation?.show()
                    }, 1000)


                } else {
                    Log.d(TAG,"limescreen 1188 副屏2")
                    mPresentation = DifferentDisplay(this, displays[0])
                    mPresentation?.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);

                    Handler().postDelayed({
                        Log.d(TAG,"limescreen 1190 副屏3")
                        mPresentation?.show()
                    }, 1000)

                }
            } catch (e: Exception) {
                Log.d(TAG,"limescreen 1195 副屏" + e.message)
            }
        } else {
            mDisplayManager.registerDisplayListener(object : DisplayManager.DisplayListener {
                override fun onDisplayAdded(displayId: Int) {
                    if (displayId != Display.DEFAULT_DISPLAY) {
                        val managerDisplay = mDisplayManager.getDisplay(displayId)
                        //add Presentation display
                        if (managerDisplay != null) {
                            //todo

                        }
                    }
                }

                override fun onDisplayRemoved(displayId: Int) {}
                override fun onDisplayChanged(displayId: Int) {}
            }, Handler(this.getMainLooper()))

            if (mPresentation != null) {
                try {
                    mPresentation?.show()
                } catch (e: Exception) {
                    Log.d(TAG,"limescreen 1281 副屏" + e.message)
                }
                Log.d(TAG,"limescreen 1220 副屏3")
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //天波二维码
        if (requestCode == 0x124) {
            if (resultCode == 0) {
                if (data != null) {
                    try {
                        //  mBeepManager.playBeepSoundAndVibrate()
                    } catch (e: java.lang.Exception) {
                        // TODO: handle exception
                        e.printStackTrace()
                    }
                    val qrcode = data.getStringExtra("qrCode")
                    //change(qrcode);
                    Toast.makeText(this@MainActivity, "Scan result:$qrcode", Toast.LENGTH_LONG)
                        .show()
                    return
                }
            } else {
                Toast.makeText(this@MainActivity, "Scan Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtils.e("主页onResume")
    }


    override fun onPause() {
        super.onPause()
        LogUtils.e("主页onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.e("主页onStop")
    }

    override fun onBackPressed() {
//        if (lastTime < System.currentTimeMillis() - Constants.DOUBLE_CLICK_EXIT_TIME) {
//            lastTime = System.currentTimeMillis()
//            showToast(R.string.tips_double_click_exit)
//            return
//        }
//        super.onBackPressed()
    }


    private fun hideAllFragment(fragmentTransaction: FragmentTransaction) {
        fragments.valueIterator().forEach {
            fragmentTransaction.hide(it)
        }
    }

    fun showPlaceHolderFragment(placeHolderFragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .replace(R.id.place_holder_content, placeHolderFragment)
                .commitNowAllowingStateLoss()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun hidePlaceHolderFragment(placeHolderFragment: Fragment) {
        try {
            supportFragmentManager.beginTransaction()
                .remove(placeHolderFragment)
                .commitNowAllowingStateLoss()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun showFragment(@HomeMenu menu: Int) {
        LogUtils.e("主页showFragment" + this)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        hideAllFragment(fragmentTransaction)
        fragmentTransaction.show(getFragment(fragmentTransaction, menu))
        fragmentTransaction.commit()
    }

    private fun getFragment(
        fragmentTransaction: FragmentTransaction,
        @HomeMenu menu: Int
    ): Fragment {
        var fragment: Fragment? = fragments[menu]
        if (fragment == null) {
            LogUtils.e("主页createFragment" + this)
            fragment = createFragment(menu)
            fragment.let {
                fragmentTransaction.add(R.id.fragmentContent, it)
                fragments.put(menu, it)
            }
        }
        return fragment
    }

    /**
     * 创建 Fragment
     */
    private fun createFragment(@HomeMenu menu: Int): Fragment = when (menu) {
        // TODO 只需修改此处，改为对应的 Fragment
        HomeMenu.MENU1 -> mainFragment
        HomeMenu.MENU2 -> StatisticsFragment.newInstance()
        HomeMenu.MENU3 -> SettingFragment.newInstance()
        //键盘设备 消费设置接口
        HomeMenu.MENU4 -> consumption1SettingFragment
        else -> throw NullPointerException()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivWifiState -> {
                if (EventBus.getDefault().isRegistered(this)) {
                    EventBus.getDefault().unregister(this);
                    LogUtils.e("EventBus unregister_打开wifi界面" + this)
                }
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
//            R.id.tvChangeMode -> {
//                EventBus.getDefault().post(MessageEventBean(MessageEventType.ModeMessage, 2))
//            }
        }
    }

    /**
     * 显示更新进度条
     * */
    fun showUpdataProgressDialog(data: CheckAppVersionBean?) {
        LogUtils.e("显示更新进度条")

        Log.d(TAG,"limecheckAppVersion 显示更新进度条 1358")

        //黑屏 begin

//        val builder = AlertDialog.Builder(this, R.style.app_dialog)
//        progressDialog = builder.create()
//        progressDialog.setCancelable(false)
//        val view = View.inflate(this, R.layout.dialog_updata_version_progress, null)
//        tvProgress = view.findViewById<TextView>(R.id.tvProgress)
//        sbProgress = view.findViewById<ProgressBar>(R.id.sbProgress)
//        sbProgress.isEnabled = false
//        progressDialog.show()
//        LogUtils.e("显示更新进度条show")
//        progressDialog.window!!.setLayout(
//            (ScreenUtils.getAppScreenWidth() * 0.32).toInt(),
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//
//        Objects.requireNonNull(progressDialog.window)!!.setContentView(view)

        //黑屏 end

        val path: String = makeDownloadPath()
        Log.d(TAG,"limecheckAppVersion 显示更新进度条 1381")
        UpdateService.Builder.create(data?.url)
            .setStoreDir(path)
            .setIcoResId(R.mipmap.ic_main_logo)
            .setIcoSmallResId(R.mipmap.ic_main_logo)
            .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
            .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
            .build(this, null)
    }

    private fun makeDownloadPath(): String {
        val path = Environment.getExternalStorageDirectory().absolutePath //+ "/Download/APK"
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
        // 清理目录中历史apk
        if (file.listFiles() != null && file.listFiles().size > 0) {
            for (f in file.listFiles()) {
                f.delete()
            }
        }
        return path
    }

    //接收事件
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 1)
    open fun onEventReceiveMsg(message: MessageEventBean) {
//        Log.d("EventBus_Subscriber", "onReceiveMsg_MAIN: " + message.toString());
        when (message.type) {

            MessageEventType.ModifyBalanceError -> {
                handlePayLogic()
            }

            MessageEventType.HeadBeat -> {
                reportDeviceStatusDisposable.dispose()
                initCheckStatus()
            }

            MessageEventType.ProgressNumber -> {
                LogUtils.e("进度条=========1427 " + message.obj)
                if ((message.obj as Int)%2 == 0) {
                    ToastUtils.showLong("App更新中... " + message.obj + "%")
                }
//                binding.mainView.visibility = View.VISIBLE
                if (progressDialog != null && progressDialog.isShowing) {
                    tvProgress.text = "" + message.obj
                    //sbProgress.progress = message.obj as Int
                }
            }

            MessageEventType.ProgressError -> {
                LogUtils.e("下载失败")
                if (progressDialog != null && progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        reportDeviceStatusDisposable.dispose()
        currentTimeDisposable?.dispose()
        netStatusDisposable?.dispose()
        mPresentation?.dismiss()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    /**
     * 全量下发蒙层
     * */
    fun showAllFsceProgressDialog() {
        LogUtils.e("全量下发蒙层")


//        val view = View.inflate(this, R.layout.dialog_all_face_progress, null)
//
//        allFaceDownDialog.show()
//        LogUtils.e("显示全量下发蒙层show")
//        allFaceDownDialog.window!!.setLayout(
//            (ScreenUtils.getAppScreenWidth() * 0.32).toInt(),
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        Objects.requireNonNull(allFaceDownDialog.window)!!.setContentView(view)
    }

    /**
     * 截获按键事件.发给ScanGunKeyEventHelper
     */
    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        try {

//        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
//            LogUtils.e("按键MainActivity back")
//            return true
//        }
            LogUtils.e("按键MainActivity keyCode: " + event.keyCode + "  device: " + event.device.name)
            //LogUtils.e("按键MainActivity device vendorId: " + event.device.vendorId + " productId: " + event.device.productId)

            // 判断当前如果是支付状态,处理USB 扫码枪键盘事件
            //if (AmountFragment.mIsPaying && (event.keyCode != KeyEvent.KEYCODE_DEL)) {
            //    keyEventResolver?.analysisKeyEvent(event)
            //    return true
            //}

            if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                if (event.action == KeyEvent.ACTION_UP) {
                    val content = keyEventResolver?.getInputCode(event)
//                EventBus.getDefault()
//                    .post(MessageEventBean(MessageEventType.KeyEventNumber, content))
                    dispatchEvent(MessageEventBean(MessageEventType.KeyEventNumber, content))
                }
                return true
            }
            if (event.keyCode == KeyEvent.KEYCODE_DPAD_UP || event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN || event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT || event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (event.action == KeyEvent.ACTION_UP) {
                    val content = keyEventResolver?.getInputCode(event)
//                EventBus.getDefault()
////                    .post(MessageEventBean(MessageEventType.KeyEventNumber, content))
                    dispatchEvent(MessageEventBean(MessageEventType.KeyEventNumber, content))
                }
                return true
            }

            if (event.action == KeyEvent.ACTION_UP) {
                val content = keyEventResolver?.getInputCode(event)
                //EventBus.getDefault().post(MessageEventBean(MessageEventType.KeyEventNumber, content))
                dispatchEvent(MessageEventBean(MessageEventType.KeyEventNumber, content))
                LogUtils.e("按键" + content)
            }

        } catch (e: Exception) {
            LogUtils.e("按键异常" + e.message)
        }
        return superDispatchKeyEvent(event)
    }

    private fun dispatchEvent(message: MessageEventBean) {
        mainFragment.amountFragment.onHandleEventMsg(message)
        consumption1SettingFragment.onHandleEventMsg(message)
        mainFragment.amountFragment.consumeStatFragment.onHandleEventMsg(message)
    }

    fun finishAll() {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        am.restartPackage(packageName)
    }

    /**
     * 刷新系统时间
     */
    private fun refreshSystemDate(formatDateStr: String) {
        try {
            val split = formatDateStr.split(" ")
            if (split.size > 2) {
                binding.tvTime.text = "${split[1]} ${split[2]}"
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 刷新网络连接状态
     */
    private fun refreshNetworkStatus(netType: Int, isConnected: Boolean) {
        if (netType == SystemEventHelper.ETHERNET_NET_TYPE) {
            if (isConnected) {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_ethernet)
            } else {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_ethernetno)
            }
        } else if (netType == SystemEventHelper.WIFI_NET_TYPE) {
            if (isConnected) {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi4)
            } else {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi0)
            }
        } else if (netType == SystemEventHelper.MOBILE_NET_TYPE) {
            if (isConnected) {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_level_4)
            } else {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_levelno)
            }
        } else {
            viewDataBinding.ivWifiState.setImageResource(0)
        }
    }

    /**
     * 刷新网络信号状态
     */
    private fun refreshNetworkRssi(netType: Int, isConnect: Boolean, level: Int) {
        if (netType == SystemEventHelper.WIFI_NET_TYPE) {
            if (isConnect) {
                if (level == 0) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi0)
                } else if (level == 1) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi1)
                } else if (level == 2) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi2)
                } else if (level == 3) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi3)
                } else if (level == 4) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi4)
                }
            } else {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_wifi0)
            }
        } else if (netType == SystemEventHelper.MOBILE_NET_TYPE) {
            if (isConnect) {
                if (level == 0) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_levelno)
                } else if (level == 1) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_level_1)
                } else if (level == 2) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_level_2)
                } else if (level == 3) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_level_3)
                } else if (level == 4) {
                    viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_level_4)
                }
            } else {
                viewDataBinding.ivWifiState.setImageResource(R.mipmap.icon_levelno)
            }
        }
    }

    private var batteryDefaultPro: Drawable? = null
    private var batteryChargingPro: Drawable? = null

    /**
     * 刷新电池相关
     */
    private fun refreshBatteryStatus(batteryLevel: Float, isCharging: Boolean) {
        if (batteryChargingPro == null) {
            batteryDefaultPro = if (batteryLevel > 20) {
                resources.getDrawable(R.drawable.battery_pro_bar_default)
            } else {
                resources.getDrawable(R.drawable.battery_pro_bar_low)
            }
            batteryChargingPro =
                resources.getDrawable(R.drawable.battery_pro_bar_charging)
        }

        if (isCharging) {
            if (batteryLevel > 20) {
                binding.ivBatteryBg.setImageResource(R.mipmap.icon_battery_ischarging)
            } else {
                binding.ivBatteryBg.setImageResource(R.mipmap.icon_battery_ischarging)
            }
            binding.pbBattery.progressDrawable = batteryChargingPro
        } else {
            binding.ivBatteryBg.setImageResource(R.mipmap.icon_battery_percent);
            binding.pbBattery.progressDrawable = batteryDefaultPro
        }
        binding.pbBattery.progress = batteryLevel.toInt()


        //Log.d(TAG, "limeisChanging isChanging == > : " + )
    }


    override fun getResources(): Resources {
        var designWidth:Int = AutoSizeConfig.getInstance().getScreenWidth()
        var designHeight:Int = AutoSizeConfig.getInstance().getScreenHeight()
        var isBaseOnWidth:Boolean = !(designWidth >designHeight)
        if(Looper.myLooper()== Looper.getMainLooper()){
            AutoSizeCompat.autoConvertDensity(super.getResources(),768f,isBaseOnWidth)
        }
        return super.getResources()
    }

    //系统事件监听
    private val systemEventListener: SystemEventHelper.OnSystemEventListener =
        object : OnSystemEventListener {
            override fun onDateTick(formatDate: String) {
                refreshSystemDate(formatDate)
            }

            override fun onDateChange(formatDate: String) {
                refreshSystemDate(formatDate)
            }

            override fun onNetworkChanged(netType: Int, isConnect: Boolean) {
                refreshNetworkStatus(netType, isConnect)
            }

            override fun onNetworkRssiChange(netType: Int, isConnect: Boolean, level: Int) {
                refreshNetworkRssi(netType, isConnect, level)
            }

            override fun onBatteryChange(batteryPercent: Float, isChanging: Boolean) {
                Log.d(TAG, "limeisChanging isChanging == > : " + isChanging)
                refreshBatteryStatus(batteryPercent, isChanging)
            }
        }

}