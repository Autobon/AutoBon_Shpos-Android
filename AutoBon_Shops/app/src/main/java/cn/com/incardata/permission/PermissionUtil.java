package cn.com.incardata.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.incardata.autobon_shops.R;


/**
 *  * @usage android >= M 的权限申请统一处理
 *
 * notice:
 * 很多手机对原生系统做了修改，比如小米4的6.0的shouldShowRequestPermissionRationale
 * 就一直返回false，而且在申请权限时，如果用户选择了拒绝，则不会再弹出对话框了, 因此有了
 *  void doAfterDenied( ... permission);
 * <p>Created by wangyang on 2018/4/12.</p>
 */
public class PermissionUtil {

    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Object mContext;

    private PermissionListener mListener;

    private List<String> mPermissionList;

    public PermissionUtil(@NonNull Object object){
        checkCallingObjectSuitability(object);
        this.mContext = object;

    }


    /**
     * 权限授权申请
     * @param hintMessage
     *              要申请的权限的提示
     *
     * @param permissions
     *              要申请的权限
     *
     * @param listener
     *              申请成功之后的callback
     */
    public void requestPermissions(@NonNull CharSequence hintMessage,
                                   @Nullable PermissionListener listener,
                                   @NonNull final String... permissions){

        if(listener != null){
            mListener = listener;
        }

        mPermissionList = new ArrayList(Arrays.asList(permissions));

        //没全部权限
        if (!hasPermissions(mContext, permissions)) {

            //需要向用户解释为什么申请这个权限
            boolean shouldShowRationale = false;
            for (int i = 0; i < mPermissionList.size(); i++){
                boolean shouldShow = shouldShowRequestPermissionRationale(mContext, mPermissionList.get(i));
                shouldShowRationale = shouldShowRationale || shouldShow;
                if (!shouldShow) {//是否已经拥有了权限
                    try {
                        mPermissionList.remove(i);
                        i--;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    continue;
                }
            }

            if (shouldShowRationale) {
                showMessageOKCancel(hintMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest(mContext, mPermissionList == null ? permissions : (String[]) mPermissionList.toArray(new String[mPermissionList.size()]),
                                REQUEST_PERMISSION_CODE);
                    }
                });
            }else {
                executePermissionsRequest(mContext, permissions,
                        REQUEST_PERMISSION_CODE);
            }
        }else if(mListener != null) { //有全部权限
            mListener.doAfterGrant(permissions);
        }
    }

    /**
     * 处理onRequestPermissionsResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                boolean allGranted = true;
                for (int grant: grantResults) {
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted && mListener != null) {

                    mListener.doAfterGrant((String[])mPermissionList.toArray(new String[mPermissionList.size()]));

                }else if(!allGranted && mListener != null){
                    mListener.doAfterDenied((String[])mPermissionList.toArray(new String[mPermissionList.size()]));
                }
                break;
        }
    }

    /**
     * 判断是否具有某权限
     * @param object
     * @param perms
     * @return
     */
    public static boolean hasPermissions(@NonNull Object object, @NonNull String... perms) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(getActivity(object), perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }


    /**
     * 兼容fragment
     * @param object
     * @param perm
     * @return
     */
    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * 执行申请,兼容fragment
     * @param object
     * @param perms
     * @param requestCode
     */
    @TargetApi(23)
    private void executePermissionsRequest(@NonNull Object object, @NonNull String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof android.support.v4.app.Fragment) {
            ((android.support.v4.app.Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * 检查传递Context是否合法
     * @param object
     */
    private void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof android.support.v4.app.Fragment;
        boolean isAppFragment = object instanceof Fragment;
        if (!(isSupportFragment || isActivity || (isAppFragment && isNeedRequest()))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }


    @TargetApi(11)
    private static Activity getActivity(@NonNull Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) object).getActivity();
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    public static boolean isNeedRequest(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void showMessageOKCancel(CharSequence message, DialogInterface.OnClickListener okListener) {
        AlertDialog dialog =  new AlertDialog.Builder(getActivity(mContext))
                .setMessage(message)
                .setPositiveButton(R.string.i_known, okListener)
//                .setNegativeButton(R.string.cancel, null)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 判断是否有权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean isPermission(Context context, String permission){
        return (ContextCompat.checkSelfPermission(context, permission)) == PackageManager.PERMISSION_GRANTED;
    }

    public interface PermissionListener {

        void doAfterGrant(String... permission);

        void doAfterDenied(String... permission);
    }
}

