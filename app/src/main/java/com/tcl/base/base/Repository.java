package com.tcl.base.base;

import java.util.HashMap;
import io.reactivex.Observable;


/**
 * Created by baixiaokang on 16/7/19.
 */
public interface Repository {
    Observable<DataArr> getData(HashMap<String, Object> param);
}
