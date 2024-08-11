package com.alex.materialdiary.ui.login

import com.alex.materialdiary.sys.net.models.get_user.UserData
import com.alex.materialdiary.sys.net.models.get_user.UserInfo

public sealed class LoginResult {

  public data class LoginSuccess internal constructor(val data: UserData?) : LoginResult()

  /**
   * Activity got cancelled by the user.
   */
  public data object LoginUserCanceled : LoginResult()


  public data class LoginError internal constructor(val exception: Exception) : LoginResult()
}