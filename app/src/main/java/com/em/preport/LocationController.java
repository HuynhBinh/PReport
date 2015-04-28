package com.em.preport;

import android.content.Context;

import java.util.List;

import greendao.MyLocation;
import greendao.MyLocationDao;
import greendao.PLocation;
import greendao.PLocationDao;
import greendao.SharePrefs;
import greendao.SharePrefsDao;

/**
 * Created by USER on 4/22/2015.
 */
public class LocationController
{
    private static PLocationDao getPLocationDAO(Context c)
    {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getPLocationDao();
    }

    private static MyLocationDao getMyLocationDAO(Context c)
    {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getMyLocationDao();
    }

    private static SharePrefsDao getSharePrefsDAO(Context c)
    {
        return ((MyApplication) c.getApplicationContext()).getDaoSession().getSharePrefsDao();
    }

    public static void insertOrUpdateSharePrefs(Context context, SharePrefs shareprefs)
    {
        getSharePrefsDAO(context).insertOrReplace(shareprefs);

    }

    public static SharePrefs getSharePrefs(Context context, String key)
    {
        return getSharePrefsDAO(context).load(key);
    }


    public static void insertPLocation(Context context, PLocation pLoc)
    {
        getPLocationDAO(context).insert(pLoc);

    }

    public static void updatePLocation(Context context, PLocation pLoc)
    {
        getPLocationDAO(context).update(pLoc);
    }


    public static void updatePLocationWithSVID(Context context, PLocation pLoc)
    {
        List<PLocation> listPLoc = getPLocationDAO(context).queryRaw("WHERE SV_ID = ?", pLoc.getSvId() + "");

        if (listPLoc.size() > 0)
        {
            PLocation pLocation = listPLoc.get(0);
            pLoc.setId(pLocation.getId());
            pLoc.setIsEnter((pLocation.getIsEnter()));
            LocationController.updatePLocation(context, pLoc);
        } else
        {
            pLoc.setIsEnter(false);
            LocationController.insertPLocation(context, pLoc);
        }

    }

    public static List<PLocation> getAllPLocation(Context context)
    {

        return getPLocationDAO(context).loadAll();

    }

    public static PLocation getPLocationById(Context context, long pLocId)
    {
        return getPLocationDAO(context).queryRaw("where svId = ?", pLocId + "").get(0);
    }

    public static void insertOrUpdateMyLocation(Context context, MyLocation mLoc)
    {
        getMyLocationDAO(context).insertOrReplace(mLoc);

    }

    public static void insertMyLocationTx(Context context, Iterable<MyLocation> mLocs)
    {
        getMyLocationDAO(context).insertInTx(mLocs);

    }

    public static void insertPLocationInBulk(Context context, Iterable<PLocation> pLocs)
    {
        getPLocationDAO(context).insertInTx(pLocs);
    }

    public static List<MyLocation> getAllMyLocation(Context context)
    {

        return getMyLocationDAO(context).loadAll();

    }


    public static MyLocation getMyLocationById(Context context, long id)
    {
        return getMyLocationDAO(context).load(id);
    }

    public static List<PLocation> getPLocationThatIsNotSyncYet(Context context)
    {
        List<PLocation> returnList = getPLocationDAO(context).queryRaw(" where is_Reported = ?", "0");
        return returnList;
    }

    public static void deleteMyLocation()
    {
        //getMyLocationDAO(context).delete
    }


}
