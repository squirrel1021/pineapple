package pineapple.bd.com.pineapple;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.bmob.v3.Bmob;

/**
 * Description : <Content><br>
 * CreateTime : 2016/3/31 14:39
 *
 * @author KevinLiu
 * @version <v1.0>
 * @Editor : KevinLiu
 * @ModifyTime : 2016/3/31 14:39
 * @ModifyDescription : <Content>
 */
public class PineApplication extends Application{

    public static PineApplication mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // 初始化 Bmob SDK
        Bmob.initialize(this, "56d23db34c49ab0334c427c034956b15");
    }

    private ConcurrentHashMap<String, WeakReference<Context>> contextObjects = new ConcurrentHashMap<String, WeakReference<Context>>();

    public synchronized Context getActiveContext(String className) {
        WeakReference<Context> ref = contextObjects.get(className);
        if (ref == null) {
            return null;
        }

        final Context c = ref.get();
        if (c == null) // If the WeakReference is no longer valid, ensure it is removed.
            contextObjects.remove(className);

        return c;
    }

    public synchronized void setActiveContext( Context context,String className) {
        //TODO
        //		if (this.contextObjects.containsKey(className)) {
        //			resetAndfinishActiveContext(className);
        //		}
        WeakReference<Context> ref = new WeakReference<Context>(context);
        this.contextObjects.put(className, ref);
    }

    public synchronized void resetActiveContext(String className) {
        contextObjects.remove(className);
    }

    public synchronized void clearActiveContext() {
        contextObjects.clear();
    }

    public synchronized void resetAndfinishActiveContext(String className) {
        Context activeContext = getActiveContext(className);
        if (null != activeContext && activeContext instanceof Activity) {
            ((Activity) activeContext).finish();
        }
        resetActiveContext(className);
    }

    public synchronized void resetAndfinishActiveContextAll() {
        Set<String> set = contextObjects.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            Context activeContext = getActiveContext(it.next());
            if (null != activeContext && activeContext instanceof Activity) {
                ((Activity) activeContext).finish();
            }
        }
        clearActiveContext();

    }

    /**
     * <p>
     * Invoked if the application is about to close. Application close is being defined as the
     * transition of the last running Activity of the current application to the Android home screen
     * using the BACK button. You can leverage this method to perform cleanup logic such as freeing
     * resources whenever your user "exits" your app using the back button.
     * </p>
     * <p>
     * Note that you must not rely on this callback as a general purpose "exit" handler, since
     * Android does not give any guarantees as to when exactly the process hosting an application is
     * being terminated. In other words, your application can be terminated at any point in time, in
     * which case this method will NOT be invoked.
     * </p>
     */
    public void onClose() {
        // NO-OP by default
        resetAndfinishActiveContextAll();
    }
}
