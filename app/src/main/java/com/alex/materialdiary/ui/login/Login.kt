package com.alex.materialdiary.ui.login

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.IntentCompat
import com.alex.materialdiary.sys.net.models.get_user.UserData
import com.alex.materialdiary.sys.net.models.get_user.UserInfo
import com.alex.materialdiary.ui.login.LoginActivity.Companion.EXTRA_RESULT_EXCEPTION
import com.alex.materialdiary.ui.login.LoginActivity.Companion.EXTRA_RESULT_INFO
import com.alex.materialdiary.ui.login.LoginActivity.Companion.RESULT_ERROR
internal fun Intent?.getRootException(): Exception {
  return this?.let { IntentCompat.getParcelableExtra(it, EXTRA_RESULT_EXCEPTION, Exception::class.java) }
    ?: IllegalStateException("Could retrieve root exception")
}
internal fun Intent?.toLoginContentType(): UserData? {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    return this?.getParcelableExtra(EXTRA_RESULT_INFO, UserData::class.java)
  } else {
    return this?.getParcelableExtra<UserData>(EXTRA_RESULT_INFO) as UserData
  }
}
public class Login : ActivityResultContract<Nothing?, LoginResult>() {

  override fun createIntent(context: Context, input: Nothing?): Intent =
    Intent(context, LoginActivity::class.java)

  override fun parseResult(resultCode: Int, intent: Intent?): LoginResult {
    return when (resultCode) {
      RESULT_OK -> LoginResult.LoginSuccess(intent.toLoginContentType())
      RESULT_CANCELED -> LoginResult.LoginUserCanceled
      RESULT_ERROR -> LoginResult.LoginError(intent.getRootException())
      else -> LoginResult.LoginError(IllegalStateException("Unknown activity result code $resultCode"))
    }
  }
}