package com.stkj.cashier.greendao.generate;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.stkj.cashier.bean.db.CompanyMemberdbEntity;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPANY_MEMBERDB_ENTITY".
*/
public class CompanyMemberdbEntityDao extends AbstractDao<CompanyMemberdbEntity, Long> {

    public static final String TABLENAME = "COMPANY_MEMBERDB_ENTITY";

    /**
     * Properties of entity CompanyMemberdbEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UniqueNumber = new Property(1, String.class, "uniqueNumber", false, "UNIQUE_NUMBER");
        public final static Property CardState = new Property(2, Integer.class, "cardState", false, "CARD_STATE");
        public final static Property Uid = new Property(3, String.class, "uid", false, "UID");
        public final static Property FullName = new Property(4, String.class, "fullName", false, "FULL_NAME");
        public final static Property DepNameType = new Property(5, String.class, "depNameType", false, "DEP_NAME_TYPE");
        public final static Property IdentityCard = new Property(6, String.class, "identityCard", false, "IDENTITY_CARD");
        public final static Property Phone = new Property(7, String.class, "phone", false, "PHONE");
        public final static Property UserNumber = new Property(8, String.class, "userNumber", false, "USER_NUMBER");
        public final static Property CardNumber = new Property(9, String.class, "cardNumber", false, "CARD_NUMBER");
        public final static Property CardType = new Property(10, String.class, "cardType", false, "CARD_TYPE");
        public final static Property Balance = new Property(11, double.class, "balance", false, "BALANCE");
        public final static Property OpeningDate = new Property(12, String.class, "openingDate", false, "OPENING_DATE");
        public final static Property LimitTimes1 = new Property(13, Integer.class, "limitTimes1", false, "LIMIT_TIMES1");
        public final static Property LimitTimes2 = new Property(14, Integer.class, "limitTimes2", false, "LIMIT_TIMES2");
        public final static Property LimitTimes3 = new Property(15, Integer.class, "limitTimes3", false, "LIMIT_TIMES3");
        public final static Property LimitTimes4 = new Property(16, Integer.class, "limitTimes4", false, "LIMIT_TIMES4");
        public final static Property ConsumptionQuota = new Property(17, double.class, "consumptionQuota", false, "CONSUMPTION_QUOTA");
        public final static Property ImgData = new Property(18, String.class, "imgData", false, "IMG_DATA");
        public final static Property AccountType = new Property(19, String.class, "AccountType", false, "ACCOUNT_TYPE");
        public final static Property CallBack = new Property(20, Boolean.class, "callBack", false, "CALL_BACK");
        public final static Property Result = new Property(21, Integer.class, "result", false, "RESULT");
        public final static Property FaceToken = new Property(22, String.class, "faceToken", false, "FACE_TOKEN");
    }


    public CompanyMemberdbEntityDao(DaoConfig config) {
        super(config);
    }
    
    public CompanyMemberdbEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPANY_MEMBERDB_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"UNIQUE_NUMBER\" TEXT," + // 1: uniqueNumber
                "\"CARD_STATE\" INTEGER," + // 2: cardState
                "\"UID\" TEXT," + // 3: uid
                "\"FULL_NAME\" TEXT," + // 4: fullName
                "\"DEP_NAME_TYPE\" TEXT," + // 5: depNameType
                "\"IDENTITY_CARD\" TEXT," + // 6: identityCard
                "\"PHONE\" TEXT," + // 7: phone
                "\"USER_NUMBER\" TEXT UNIQUE ," + // 8: userNumber
                "\"CARD_NUMBER\" TEXT," + // 9: cardNumber
                "\"CARD_TYPE\" TEXT," + // 10: cardType
                "\"BALANCE\" REAL NOT NULL ," + // 11: balance
                "\"OPENING_DATE\" TEXT," + // 12: openingDate
                "\"LIMIT_TIMES1\" INTEGER," + // 13: limitTimes1
                "\"LIMIT_TIMES2\" INTEGER," + // 14: limitTimes2
                "\"LIMIT_TIMES3\" INTEGER," + // 15: limitTimes3
                "\"LIMIT_TIMES4\" INTEGER," + // 16: limitTimes4
                "\"CONSUMPTION_QUOTA\" REAL NOT NULL ," + // 17: consumptionQuota
                "\"IMG_DATA\" TEXT," + // 18: imgData
                "\"ACCOUNT_TYPE\" TEXT," + // 19: AccountType
                "\"CALL_BACK\" INTEGER," + // 20: callBack
                "\"RESULT\" INTEGER," + // 21: result
                "\"FACE_TOKEN\" TEXT);"); // 22: faceToken
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPANY_MEMBERDB_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CompanyMemberdbEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uniqueNumber = entity.getUniqueNumber();
        if (uniqueNumber != null) {
            stmt.bindString(2, uniqueNumber);
        }
 
        Integer cardState = entity.getCardState();
        if (cardState != null) {
            stmt.bindLong(3, cardState);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(4, uid);
        }
 
        String fullName = entity.getFullName();
        if (fullName != null) {
            stmt.bindString(5, fullName);
        }
 
        String depNameType = entity.getDepNameType();
        if (depNameType != null) {
            stmt.bindString(6, depNameType);
        }
 
        String identityCard = entity.getIdentityCard();
        if (identityCard != null) {
            stmt.bindString(7, identityCard);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
 
        String userNumber = entity.getUserNumber();
        if (userNumber != null) {
            stmt.bindString(9, userNumber);
        }
 
        String cardNumber = entity.getCardNumber();
        if (cardNumber != null) {
            stmt.bindString(10, cardNumber);
        }
 
        String cardType = entity.getCardType();
        if (cardType != null) {
            stmt.bindString(11, cardType);
        }
        stmt.bindDouble(12, entity.getBalance());
 
        String openingDate = entity.getOpeningDate();
        if (openingDate != null) {
            stmt.bindString(13, openingDate);
        }
 
        Integer limitTimes1 = entity.getLimitTimes1();
        if (limitTimes1 != null) {
            stmt.bindLong(14, limitTimes1);
        }
 
        Integer limitTimes2 = entity.getLimitTimes2();
        if (limitTimes2 != null) {
            stmt.bindLong(15, limitTimes2);
        }
 
        Integer limitTimes3 = entity.getLimitTimes3();
        if (limitTimes3 != null) {
            stmt.bindLong(16, limitTimes3);
        }
 
        Integer limitTimes4 = entity.getLimitTimes4();
        if (limitTimes4 != null) {
            stmt.bindLong(17, limitTimes4);
        }
        stmt.bindDouble(18, entity.getConsumptionQuota());
 
        String imgData = entity.getImgData();
        if (imgData != null) {
            stmt.bindString(19, imgData);
        }
 
        String AccountType = entity.getAccountType();
        if (AccountType != null) {
            stmt.bindString(20, AccountType);
        }
 
        Boolean callBack = entity.getCallBack();
        if (callBack != null) {
            stmt.bindLong(21, callBack ? 1L: 0L);
        }
 
        Integer result = entity.getResult();
        if (result != null) {
            stmt.bindLong(22, result);
        }
 
        String faceToken = entity.getFaceToken();
        if (faceToken != null) {
            stmt.bindString(23, faceToken);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CompanyMemberdbEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String uniqueNumber = entity.getUniqueNumber();
        if (uniqueNumber != null) {
            stmt.bindString(2, uniqueNumber);
        }
 
        Integer cardState = entity.getCardState();
        if (cardState != null) {
            stmt.bindLong(3, cardState);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(4, uid);
        }
 
        String fullName = entity.getFullName();
        if (fullName != null) {
            stmt.bindString(5, fullName);
        }
 
        String depNameType = entity.getDepNameType();
        if (depNameType != null) {
            stmt.bindString(6, depNameType);
        }
 
        String identityCard = entity.getIdentityCard();
        if (identityCard != null) {
            stmt.bindString(7, identityCard);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
 
        String userNumber = entity.getUserNumber();
        if (userNumber != null) {
            stmt.bindString(9, userNumber);
        }
 
        String cardNumber = entity.getCardNumber();
        if (cardNumber != null) {
            stmt.bindString(10, cardNumber);
        }
 
        String cardType = entity.getCardType();
        if (cardType != null) {
            stmt.bindString(11, cardType);
        }
        stmt.bindDouble(12, entity.getBalance());
 
        String openingDate = entity.getOpeningDate();
        if (openingDate != null) {
            stmt.bindString(13, openingDate);
        }
 
        Integer limitTimes1 = entity.getLimitTimes1();
        if (limitTimes1 != null) {
            stmt.bindLong(14, limitTimes1);
        }
 
        Integer limitTimes2 = entity.getLimitTimes2();
        if (limitTimes2 != null) {
            stmt.bindLong(15, limitTimes2);
        }
 
        Integer limitTimes3 = entity.getLimitTimes3();
        if (limitTimes3 != null) {
            stmt.bindLong(16, limitTimes3);
        }
 
        Integer limitTimes4 = entity.getLimitTimes4();
        if (limitTimes4 != null) {
            stmt.bindLong(17, limitTimes4);
        }
        stmt.bindDouble(18, entity.getConsumptionQuota());
 
        String imgData = entity.getImgData();
        if (imgData != null) {
            stmt.bindString(19, imgData);
        }
 
        String AccountType = entity.getAccountType();
        if (AccountType != null) {
            stmt.bindString(20, AccountType);
        }
 
        Boolean callBack = entity.getCallBack();
        if (callBack != null) {
            stmt.bindLong(21, callBack ? 1L: 0L);
        }
 
        Integer result = entity.getResult();
        if (result != null) {
            stmt.bindLong(22, result);
        }
 
        String faceToken = entity.getFaceToken();
        if (faceToken != null) {
            stmt.bindString(23, faceToken);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CompanyMemberdbEntity readEntity(Cursor cursor, int offset) {
        CompanyMemberdbEntity entity = new CompanyMemberdbEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // uniqueNumber
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // cardState
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // uid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // fullName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // depNameType
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // identityCard
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // phone
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // userNumber
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // cardNumber
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // cardType
            cursor.getDouble(offset + 11), // balance
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // openingDate
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // limitTimes1
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // limitTimes2
            cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15), // limitTimes3
            cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16), // limitTimes4
            cursor.getDouble(offset + 17), // consumptionQuota
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // imgData
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // AccountType
            cursor.isNull(offset + 20) ? null : cursor.getShort(offset + 20) != 0, // callBack
            cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21), // result
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22) // faceToken
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CompanyMemberdbEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUniqueNumber(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCardState(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setUid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFullName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDepNameType(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIdentityCard(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPhone(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUserNumber(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCardNumber(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCardType(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setBalance(cursor.getDouble(offset + 11));
        entity.setOpeningDate(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setLimitTimes1(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setLimitTimes2(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setLimitTimes3(cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15));
        entity.setLimitTimes4(cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16));
        entity.setConsumptionQuota(cursor.getDouble(offset + 17));
        entity.setImgData(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setAccountType(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCallBack(cursor.isNull(offset + 20) ? null : cursor.getShort(offset + 20) != 0);
        entity.setResult(cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21));
        entity.setFaceToken(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CompanyMemberdbEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CompanyMemberdbEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CompanyMemberdbEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
