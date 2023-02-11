package com.alex.materialdiary.sys.common.cryptor;
//com/alex/materialdiary/sys/common/cryptor/SuperCrypt

import static xdroid.toaster.Toaster.toast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.Signature;
import android.content.pm.VersionedPackage;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.security.cert.CertificateException;


public class SuperCrypt {
    public static Context context;

    public static void setContext(Context context) {
        SuperCrypt.context = context;
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /*public SuperCrypt(){
        System.loadLibrary("dianasilna");
    }
    public native String cry(String str);*/
    /*public PackageManager getPackageManager(){
        return new PackageManager() {
            @Override
            public PackageInfo getPackageInfo(@NonNull String packageName, int flags) throws NameNotFoundException {
                PackageInfo pg = context.getPackageManager().getPackageInfo("com.alex.materialdiary", flags);

                //KeyHelper.get(context, "SHA1");
                String hex = "3082058830820370a0030201020214597e3f8c38c4e24b14029528c500886d370a5c26300d06092a864886f70d01010b05003074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f69643020170d3232303530383136303332375a180f32303532303530383136303332375a3074310b3009060355040613025553311330110603550408130a43616c69666f726e6961311630140603550407130d4d6f756e7461696e205669657731143012060355040a130b476f6f676c6520496e632e3110300e060355040b1307416e64726f69643110300e06035504031307416e64726f696430820222300d06092a864886f70d01010105000382020f003082020a0282020100ccd6e256629cfdedc55af643717155b9f7f44fb50a85749500021c71a92ff5c9ad5f58793e756aea8dfc1918de18e35302ebcc10e77d374f47ff90e4a49e415b940cb2a1e1707c2825d6edd546d5d209b976bf77cce963ad602708b00798e80c456bb5e4eff4a843fb63cdcb8aef8b82d4114a10176c4f87507e99c6a0bca967974186ee6d20478d89ee4be93460c773e0ce3c6e2c81eabca95d8dda97de5dc82c2a9d5aace3a540a02608b192e897300205815b4ecb33b1df11267a90c5b2767325a4c8c7328e343907d07fd93c11ce134bf1efffa20f533e9577ccba06113674475d04cfc1ac318ac0fb1838aa0c8933a413a952c21b1343feba014670cfc3318b2264cebc9bf923fa26667190f79f2daade078e61cded6417433b4d1e87921d1644b4c52561e013e6ef87d55f12ffca6630b1bb54477a71ab53bb73c6e6be54121718a3e51275dfde79a21e24114df73b5ce8c6349a1fa0e217c6811987b501eff84ad062c774b899bf5bca5957eaf44ee92288e66f400959fa3296f98a13d275e091b615c29cc7a30e029720a54deb8d11d159e5cf490c64b361b4f4dd1135cc6f36827302b508f6c304481c74a5a2b11d7c5dd6185145ef668b7008348ef78d432b1fd61247ef2d52dff4546373752f399debbabe6107ef13880a367090f404023cc9c3c0288f93f3db216d2837578f4c70281146d94464d61e0dfa57090203010001a310300e300c0603551d13040530030101ff300d06092a864886f70d01010b0500038202010032bdfe5a282bdedea7c12e36ea4c3755625000d6ae6634e7841eab2556453ea503830d15110b8018d597a375ae389ae95ea2952622d9acb0596c4f6d1d28e1c20de21a5ff8cfaaab9855fe878affcc2ec4328565efa600e43dd32d24dda3dc1632c10ce66086a075381af8a1682f8d6476f90b3b4cc3c17c1bd814358c39ec86da165ed2cb96b661f1e59b51f3bd34d014d4540cee2fd3a3b9993784797bed081fc14f1467b3ef06ee6869d37ecec4ab57e8d908bcd09a4c8c1417cac4e63f2b28b95d46dc419817691093c939bc5b060ed789b169ac8db7e484362bf2ab1863fc4272a7f30c0d83572a42d5e3e031601a53da6f4c5b2a44cab7cdc346d74d59990d2ce75b6a2825744d1cff5fe092d4f565928ce718e400a347a95bc11e878dd34a2802db52341e88a76e8caa941ced8c46e23602214b48dee7285ddd79ef73e611f137d93c2cfda9647399753554d8d18a09d9153603ee4f95c4a23e247db09649f5e950ca745817ffc52b3202d815a83a751ef903cdb31836681c81c2f5a57fafca4350b54312eedb2bc018e45350a1ef1670d1f799fb275d32ed8dd4ebb0af25cedba804b78f13e475b3418b40ecad51db58a1987b7b53014525af3d7943522749d5d1f83f75563404a684421cb09e2f0123445798edbf0b1067c39e3e243794d719219a93df5628c509dcb7f371b0b82437ece55c31f4b89d488658d6dd";
                //pg.signatures[0] = new Signature(hexStringToByteArray(hex));
                //try {
                //    Log.e("cert", String.valueOf(javax.security.cert.X509Certificate.getInstance(hexStringToByteArray(hex)).getIssuerDN().toString()));
                //} catch (CertificateException e) {
                //    e.printStackTrace();
                //}
                return pg;
            }

            @Override
            public PackageInfo getPackageInfo(@NonNull VersionedPackage versionedPackage, int flags) throws NameNotFoundException {
                return new PackageInfo();
            }

            @Override
            public String[] currentToCanonicalPackageNames(@NonNull String[] packageNames) {
                return new String[0];
            }

            @Override
            public String[] canonicalToCurrentPackageNames(@NonNull String[] packageNames) {
                return new String[0];
            }

            @Nullable
            @Override
            public Intent getLaunchIntentForPackage(@NonNull String packageName) {
                return new Intent();
            }

            @Nullable
            @Override
            public Intent getLeanbackLaunchIntentForPackage(@NonNull String packageName) {
                return null;
            }

            @Override
            public int[] getPackageGids(@NonNull String packageName) throws NameNotFoundException {
                return new int[0];
            }

            @Override
            public int[] getPackageGids(@NonNull String packageName, int flags) throws NameNotFoundException {
                return new int[0];
            }

            @Override
            public int getPackageUid(@NonNull String packageName, int flags) throws NameNotFoundException {
                return 0;
            }

            @Override
            public PermissionInfo getPermissionInfo(@NonNull String permName, int flags) throws NameNotFoundException {
                return new PermissionInfo();
            }

            @NonNull
            @Override
            public List<PermissionInfo> queryPermissionsByGroup(@Nullable String permissionGroup, int flags) throws NameNotFoundException {
                return new ArrayList<>();
            }

            @NonNull
            @Override
            public PermissionGroupInfo getPermissionGroupInfo(@NonNull String groupName, int flags) throws NameNotFoundException {
                return new PermissionGroupInfo();
            }

            @NonNull
            @Override
            public List<PermissionGroupInfo> getAllPermissionGroups(int flags) {
                return new ArrayList<>();
            }

            @NonNull
            @Override
            public ApplicationInfo getApplicationInfo(@NonNull String packageName, int flags) throws NameNotFoundException {
                return new ApplicationInfo();
            }

            @NonNull
            @Override
            public ActivityInfo getActivityInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
                return new ActivityInfo();
            }

            @NonNull
            @Override
            public ActivityInfo getReceiverInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
                return new ActivityInfo();
            }

            @NonNull
            @Override
            public ServiceInfo getServiceInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
                return new ServiceInfo();
            }

            @NonNull
            @Override
            public ProviderInfo getProviderInfo(@NonNull ComponentName component, int flags) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<PackageInfo> getInstalledPackages(int flags) {
                return new ArrayList<>();
            }

            @NonNull
            @Override
            public List<PackageInfo> getPackagesHoldingPermissions(@NonNull String[] permissions, int flags) {
                return new ArrayList<>();
            }

            @Override
            public int checkPermission(@NonNull String permName, @NonNull String packageName) {
                return PackageManager.PERMISSION_GRANTED;
            }

            @Override
            public boolean isPermissionRevokedByPolicy(@NonNull String permName, @NonNull String packageName) {
                return false;
            }

            @Override
            public boolean addPermission(@NonNull PermissionInfo info) {
                return false;
            }

            @Override
            public boolean addPermissionAsync(@NonNull PermissionInfo info) {
                return false;
            }

            @Override
            public void removePermission(@NonNull String permName) {

            }

            @Override
            public int checkSignatures(@NonNull String packageName1, @NonNull String packageName2) {
                return 0;
            }

            @Override
            public int checkSignatures(int uid1, int uid2) {
                return 0;
            }

            @Nullable
            @Override
            public String[] getPackagesForUid(int uid) {
                return new String[0];
            }

            @Nullable
            @Override
            public String getNameForUid(int uid) {
                return null;
            }

            @NonNull
            @Override
            public List<ApplicationInfo> getInstalledApplications(int flags) {
                return null;
            }

            @Override
            public boolean isInstantApp() {
                return false;
            }

            @Override
            public boolean isInstantApp(@NonNull String packageName) {
                return false;
            }

            @Override
            public int getInstantAppCookieMaxBytes() {
                return 0;
            }

            @NonNull
            @Override
            public byte[] getInstantAppCookie() {
                return new byte[0];
            }

            @Override
            public void clearInstantAppCookie() {

            }

            @Override
            public void updateInstantAppCookie(@Nullable byte[] cookie) {

            }

            @Nullable
            @Override
            public String[] getSystemSharedLibraryNames() {
                return new String[0];
            }

            @NonNull
            @Override
            public List<SharedLibraryInfo> getSharedLibraries(int flags) {
                return null;
            }

            @Nullable
            @Override
            public ChangedPackages getChangedPackages(int sequenceNumber) {
                return null;
            }

            @NonNull
            @Override
            public FeatureInfo[] getSystemAvailableFeatures() {
                return new FeatureInfo[0];
            }

            @Override
            public boolean hasSystemFeature(@NonNull String featureName) {
                return false;
            }

            @Override
            public boolean hasSystemFeature(@NonNull String featureName, int version) {
                return false;
            }

            @Nullable
            @Override
            public ResolveInfo resolveActivity(@NonNull Intent intent, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentActivities(@NonNull Intent intent, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentActivityOptions(@Nullable ComponentName caller, @Nullable Intent[] specifics, @NonNull Intent intent, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryBroadcastReceivers(@NonNull Intent intent, int flags) {
                return null;
            }

            @Nullable
            @Override
            public ResolveInfo resolveService(@NonNull Intent intent, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentServices(@NonNull Intent intent, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ResolveInfo> queryIntentContentProviders(@NonNull Intent intent, int flags) {
                return null;
            }

            @Nullable
            @Override
            public ProviderInfo resolveContentProvider(@NonNull String authority, int flags) {
                return null;
            }

            @NonNull
            @Override
            public List<ProviderInfo> queryContentProviders(@Nullable String processName, int uid, int flags) {
                return null;
            }

            @NonNull
            @Override
            public InstrumentationInfo getInstrumentationInfo(@NonNull ComponentName className, int flags) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public List<InstrumentationInfo> queryInstrumentation(@NonNull String targetPackage, int flags) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getDrawable(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getActivityIcon(@NonNull ComponentName activityName) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getActivityIcon(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityBanner(@NonNull ComponentName activityName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityBanner(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getDefaultActivityIcon() {
                return null;
            }

            @NonNull
            @Override
            public Drawable getApplicationIcon(@NonNull ApplicationInfo info) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getApplicationIcon(@NonNull String packageName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationBanner(@NonNull ApplicationInfo info) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationBanner(@NonNull String packageName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityLogo(@NonNull ComponentName activityName) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getActivityLogo(@NonNull Intent intent) throws NameNotFoundException {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationLogo(@NonNull ApplicationInfo info) {
                return null;
            }

            @Nullable
            @Override
            public Drawable getApplicationLogo(@NonNull String packageName) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Drawable getUserBadgedIcon(@NonNull Drawable drawable, @NonNull UserHandle user) {
                return null;
            }

            @NonNull
            @Override
            public Drawable getUserBadgedDrawableForDensity(@NonNull Drawable drawable, @NonNull UserHandle user, @Nullable Rect badgeLocation, int badgeDensity) {
                return null;
            }

            @NonNull
            @Override
            public CharSequence getUserBadgedLabel(@NonNull CharSequence label, @NonNull UserHandle user) {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getText(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
                return null;
            }

            @Nullable
            @Override
            public XmlResourceParser getXml(@NonNull String packageName, int resid, @Nullable ApplicationInfo appInfo) {
                return null;
            }

            @NonNull
            @Override
            public CharSequence getApplicationLabel(@NonNull ApplicationInfo info) {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForActivity(@NonNull ComponentName activityName) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForApplication(@NonNull ApplicationInfo app) throws NameNotFoundException {
                return null;
            }

            @NonNull
            @Override
            public Resources getResourcesForApplication(@NonNull String packageName) throws NameNotFoundException {
                return null;
            }

            @Override
            public void verifyPendingInstall(int id, int verificationCode) {

            }

            @Override
            public void extendVerificationTimeout(int id, int verificationCodeAtTimeout, long millisecondsToDelay) {

            }

            @Override
            public void setInstallerPackageName(@NonNull String targetPackage, @Nullable String installerPackageName) {

            }

            @Nullable
            @Override
            public String getInstallerPackageName(@NonNull String packageName) {
                return "dddd";
            }

            @Override
            public void addPackageToPreferred(@NonNull String packageName) {

            }

            @Override
            public void removePackageFromPreferred(@NonNull String packageName) {
            }

            @NonNull
            @Override
            public List<PackageInfo> getPreferredPackages(int flags) {
                return new ArrayList<>();
            }

            @Override
            public void addPreferredActivity(@NonNull IntentFilter filter, int match, @Nullable ComponentName[] set, @NonNull ComponentName activity) {

            }

            @Override
            public void clearPackagePreferredActivities(@NonNull String packageName) {

            }

            @Override
            public int getPreferredActivities(@NonNull List<IntentFilter> outFilters, @NonNull List<ComponentName> outActivities, @Nullable String packageName) {
                return 0;
            }

            @Override
            public void setComponentEnabledSetting(@NonNull ComponentName componentName, int newState, int flags) {

            }

            @Override
            public int getComponentEnabledSetting(@NonNull ComponentName componentName) {
                return PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
            }

            @Override
            public void setApplicationEnabledSetting(@NonNull String packageName, int newState, int flags) {

            }

            @Override
            public int getApplicationEnabledSetting(@NonNull String packageName) {
                return PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
            }

            @Override
            public boolean isSafeMode() {
                return false;
            }

            @Override
            public void setApplicationCategoryHint(@NonNull String packageName, int categoryHint) {

            }

            @NonNull
            @Override
            public PackageInstaller getPackageInstaller() {
                return null;
            }

            @Override
            public boolean canRequestPackageInstalls() {
                return false;
            }
        };
    }*/
    public PackageManager getPackageManager(){
        return context.getPackageManager();
    }
    /*public String getPackageName(){
        return "ru.integrics.mobileschool";
    }*/
/*
    public String get(String str){
        Log.e("my vk/tg", "@dianasilna");
        toast(cry(str.substring(0, str.length() / 2)));
        toast(cry(str));
        String key = Base64.encodeToString(cry(str.substring(0, str.length() / 2)).getBytes(), Base64.NO_WRAP);
        return key;
    }*/

}
