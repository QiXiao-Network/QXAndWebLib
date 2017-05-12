package com.qixiao.qxweblib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.qixiao.qxweblib.R;

import java.util.List;

/**
 * Created by liu jun on 2017/5/12.
 */

public class Utils {

    public static Intent newEmailIntent(String toAddress, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + Uri.encode(toAddress) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body) + "&cc=" + Uri.encode(cc)));
        return intent;
    }

    public static Intent newTelIntent(String telurl) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(telurl));
        return intent;
    }

    public static void execIntentIfSafe(Context context, Intent intent) {
        if(Utils.isIntentSafe(context, intent)) {
            context.startActivity(intent);
        }
        else {
            Toast.makeText(context, "No related activity found!", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isIntentSafe(Context context, Intent intent) {
        // Verify it resolves
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }

}
