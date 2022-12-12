package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.Field;

class ResourcesFlusher {
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

    ResourcesFlusher() {
    }

    static boolean flush(@NonNull Resources resources) {
        if (Build.VERSION.SDK_INT >= 24) {
            return flushNougats(resources);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return flushMarshmallows(resources);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return flushLollipops(resources);
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.Map} */
    /* JADX WARNING: Multi-variable type inference failed */
    @android.support.annotation.RequiresApi(21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean flushLollipops(@android.support.annotation.NonNull android.content.res.Resources r6) {
        /*
            r4 = 1
            boolean r3 = sDrawableCacheFieldFetched
            if (r3 != 0) goto L_0x0017
            java.lang.Class<android.content.res.Resources> r3 = android.content.res.Resources.class
            java.lang.String r5 = "mDrawableCache"
            java.lang.reflect.Field r3 = r3.getDeclaredField(r5)     // Catch:{ NoSuchFieldException -> 0x002d }
            sDrawableCacheField = r3     // Catch:{ NoSuchFieldException -> 0x002d }
            java.lang.reflect.Field r3 = sDrawableCacheField     // Catch:{ NoSuchFieldException -> 0x002d }
            r5 = 1
            r3.setAccessible(r5)     // Catch:{ NoSuchFieldException -> 0x002d }
        L_0x0015:
            sDrawableCacheFieldFetched = r4
        L_0x0017:
            java.lang.reflect.Field r3 = sDrawableCacheField
            if (r3 == 0) goto L_0x003f
            r1 = 0
            java.lang.reflect.Field r3 = sDrawableCacheField     // Catch:{ IllegalAccessException -> 0x0036 }
            java.lang.Object r3 = r3.get(r6)     // Catch:{ IllegalAccessException -> 0x0036 }
            r0 = r3
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ IllegalAccessException -> 0x0036 }
            r1 = r0
        L_0x0026:
            if (r1 == 0) goto L_0x003f
            r1.clear()
            r3 = r4
        L_0x002c:
            return r3
        L_0x002d:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve Resources#mDrawableCache field"
            android.util.Log.e(r3, r5, r2)
            goto L_0x0015
        L_0x0036:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve value from Resources#mDrawableCache"
            android.util.Log.e(r3, r5, r2)
            goto L_0x0026
        L_0x003f:
            r3 = 0
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.ResourcesFlusher.flushLollipops(android.content.res.Resources):boolean");
    }

    @RequiresApi(23)
    private static boolean flushMarshmallows(@NonNull Resources resources) {
        boolean z = true;
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mDrawableCache field", e);
            }
            sDrawableCacheFieldFetched = true;
        }
        Object drawableCache = null;
        if (sDrawableCacheField != null) {
            try {
                drawableCache = sDrawableCacheField.get(resources);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Could not retrieve value from Resources#mDrawableCache", e2);
            }
        }
        if (drawableCache == null) {
            return false;
        }
        if (drawableCache == null || !flushThemedResourcesCache(drawableCache)) {
            z = false;
        }
        return z;
    }

    @RequiresApi(24)
    private static boolean flushNougats(@NonNull Resources resources) {
        boolean z = true;
        if (!sResourcesImplFieldFetched) {
            try {
                sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
                sResourcesImplField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mResourcesImpl field", e);
            }
            sResourcesImplFieldFetched = true;
        }
        if (sResourcesImplField == null) {
            return false;
        }
        Object resourcesImpl = null;
        try {
            resourcesImpl = sResourcesImplField.get(resources);
        } catch (IllegalAccessException e2) {
            Log.e(TAG, "Could not retrieve value from Resources#mResourcesImpl", e2);
        }
        if (resourcesImpl == null) {
            return false;
        }
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = resourcesImpl.getClass().getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            } catch (NoSuchFieldException e3) {
                Log.e(TAG, "Could not retrieve ResourcesImpl#mDrawableCache field", e3);
            }
            sDrawableCacheFieldFetched = true;
        }
        Object drawableCache = null;
        if (sDrawableCacheField != null) {
            try {
                drawableCache = sDrawableCacheField.get(resourcesImpl);
            } catch (IllegalAccessException e4) {
                Log.e(TAG, "Could not retrieve value from ResourcesImpl#mDrawableCache", e4);
            }
        }
        if (drawableCache == null || !flushThemedResourcesCache(drawableCache)) {
            z = false;
        }
        return z;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.util.LongSparseArray} */
    /* JADX WARNING: Multi-variable type inference failed */
    @android.support.annotation.RequiresApi(16)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean flushThemedResourcesCache(@android.support.annotation.NonNull java.lang.Object r8) {
        /*
            r5 = 0
            r6 = 1
            boolean r4 = sThemedResourceCacheClazzFetched
            if (r4 != 0) goto L_0x0010
            java.lang.String r4 = "android.content.res.ThemedResourceCache"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x0016 }
            sThemedResourceCacheClazz = r4     // Catch:{ ClassNotFoundException -> 0x0016 }
        L_0x000e:
            sThemedResourceCacheClazzFetched = r6
        L_0x0010:
            java.lang.Class r4 = sThemedResourceCacheClazz
            if (r4 != 0) goto L_0x001f
            r4 = r5
        L_0x0015:
            return r4
        L_0x0016:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r7 = "Could not find ThemedResourceCache class"
            android.util.Log.e(r4, r7, r1)
            goto L_0x000e
        L_0x001f:
            boolean r4 = sThemedResourceCache_mUnthemedEntriesFieldFetched
            if (r4 != 0) goto L_0x0035
            java.lang.Class r4 = sThemedResourceCacheClazz     // Catch:{ NoSuchFieldException -> 0x003b }
            java.lang.String r7 = "mUnthemedEntries"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r7)     // Catch:{ NoSuchFieldException -> 0x003b }
            sThemedResourceCache_mUnthemedEntriesField = r4     // Catch:{ NoSuchFieldException -> 0x003b }
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField     // Catch:{ NoSuchFieldException -> 0x003b }
            r7 = 1
            r4.setAccessible(r7)     // Catch:{ NoSuchFieldException -> 0x003b }
        L_0x0033:
            sThemedResourceCache_mUnthemedEntriesFieldFetched = r6
        L_0x0035:
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField
            if (r4 != 0) goto L_0x0044
            r4 = r5
            goto L_0x0015
        L_0x003b:
            r2 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r7 = "Could not retrieve ThemedResourceCache#mUnthemedEntries field"
            android.util.Log.e(r4, r7, r2)
            goto L_0x0033
        L_0x0044:
            r3 = 0
            java.lang.reflect.Field r4 = sThemedResourceCache_mUnthemedEntriesField     // Catch:{ IllegalAccessException -> 0x0056 }
            java.lang.Object r4 = r4.get(r8)     // Catch:{ IllegalAccessException -> 0x0056 }
            r0 = r4
            android.util.LongSparseArray r0 = (android.util.LongSparseArray) r0     // Catch:{ IllegalAccessException -> 0x0056 }
            r3 = r0
        L_0x004f:
            if (r3 == 0) goto L_0x005f
            r3.clear()
            r4 = r6
            goto L_0x0015
        L_0x0056:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r7 = "Could not retrieve value from ThemedResourceCache#mUnthemedEntries"
            android.util.Log.e(r4, r7, r1)
            goto L_0x004f
        L_0x005f:
            r4 = r5
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.ResourcesFlusher.flushThemedResourcesCache(java.lang.Object):boolean");
    }
}
