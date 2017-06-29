package com.wiatec.blive.module

import com.wiatec.blive.model.UserData
import dagger.Module
import dagger.Provides

/**
 * Created by patrick on 29/05/2017.
 * create time : 6:00 PM
 */
@Module
class DataModule {

    @Provides
    fun providerUserData(): UserData{
        val userData: UserData = UserData()
        return userData
    }
}