package com.aries.library.fast.demo.retrofit.repository;

import com.aries.library.fast.demo.App;
import com.aries.library.fast.demo.base.BaseMovieEntity;
import com.aries.library.fast.demo.entity.UpdateEntity;
import com.aries.library.fast.demo.retrofit.service.ApiService;
import com.aries.library.fast.retrofit.FastRetrofit;
import com.aries.library.fast.retrofit.FastTransformer;
import com.aries.library.fast.util.FastUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * Created: AriesHoo on 2017/9/7 11:00
 * Function:Retrofit api调用示例
 * Desc:
 */

public class ApiRepository extends BaseRepository {

    private static volatile ApiRepository instance;
    private ApiService mApiService;

    private ApiRepository() {
        mApiService = getApiService();
    }

    public static ApiRepository getInstance() {
        if (instance == null) {
            synchronized (ApiRepository.class) {
                if (instance == null) {
                    instance = new ApiRepository();
                }
            }
        }
        return instance;
    }

    private ApiService getApiService() {
        mApiService = FastRetrofit.getInstance().createService(ApiService.class);
        return mApiService;
    }

    /**
     * 获取电影列表
     *
     * @param url   拼接URL
     * @param start 起始 下标
     * @param count 请求总数量
     * @return
     */
    public Observable<BaseMovieEntity> getMovie(String url, int start, int count) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("start", start);
        params.put("count", count);
        return FastTransformer.switchSchedulers(getApiService().getMovie(url, params));
    }

    /**
     * 检查版本--是否传递本地App 版本相关信息根据具体接口而定(demo这里是可以不需要传的,所有判断逻辑放在app端--不推荐)
     *
     * @return
     */
    public Observable<UpdateEntity> updateApp() {
        Map<String, Object> params = new HashMap<>(2);
        params.put("versionCode", FastUtil.getVersionCode(App.getContext()));
        params.put("versionName", FastUtil.getVersionName(App.getContext()));
        return FastTransformer.switchSchedulers(getApiService().updateApp(params));
    }
}
