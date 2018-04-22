package com.diezsiete.lscapp.data;

import com.diezsiete.lscapp.data.db.model.Level;

import retrofit2.Call;

/**
 * TODO : documentation
 * @param <T>
 */
public interface DataManagerResponse<T> {
    void onResponse(T response);
    void onFailure(Throwable t);
}
