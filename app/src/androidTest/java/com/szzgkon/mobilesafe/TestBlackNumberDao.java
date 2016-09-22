package com.szzgkon.mobilesafe;

import android.content.Context;
import android.test.AndroidTestCase;

import com.szzgkon.mobilesafe.bean.BlackNumberInfo;
import com.szzgkon.mobilesafe.db.dao.BlackNumberDao;

import java.util.List;
import java.util.Random;

/**
 * Created by zhangyongke on 16/9/21.
 */
public class TestBlackNumberDao extends AndroidTestCase{

   public Context mContext;

    @Override
    protected void setUp() throws Exception {
        this.mContext = getContext();
        super.setUp();
    }

    public void testAdd(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
             Long number = 13300000000l + i;
            dao.add(number+"",String.valueOf(random.nextInt(3)+1));
        }

    }
    public void testDelete(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        boolean delete = dao.delete("13300000000");
        assertEquals(true,delete);
    }
    public void testFind(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        String number = dao.findNumber("13300000001");

    }
    public void testFindAll(){
        BlackNumberDao dao = new BlackNumberDao(mContext);
        List<BlackNumberInfo> blackNumberInfos = dao.findAll();
        for (BlackNumberInfo blackNumberInfo: blackNumberInfos) {
            System.out.println(blackNumberInfo.getMode() + "" + blackNumberInfo.getNumber());
        }
    }
}
