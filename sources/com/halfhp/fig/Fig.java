package com.halfhp.fig;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class Fig {
    private static final String CFG_ELEMENT_NAME = "config";
    private static final String COLOR_TRANSPARENT_COMPRESSED_HEX = "#0";
    private static final String DOT_SEPARATOR = ".";
    private static final String GETTER_PREFIX = "get";
    private static final String PATH_SEPARATOR = "/";
    private static final String RESOURCE_ID_PREFIX = "@";
    private static final String SETTER_PREFIX = "set";

    private static int parseResId(Context ctx, String value) {
        if (!value.startsWith(RESOURCE_ID_PREFIX)) {
            throw new IllegalArgumentException();
        } else if (!value.contains(PATH_SEPARATOR)) {
            return Integer.parseInt(value.substring(1));
        } else {
            String[] split = value.split(PATH_SEPARATOR);
            String pack = split[0].replace(RESOURCE_ID_PREFIX, "");
            return ctx.getResources().getIdentifier(split[1], pack, ctx.getPackageName());
        }
    }

    private static int parseIntAttr(String value) {
        if (Character.isDigit(value.charAt(0))) {
            return Integer.parseInt(value);
        }
        if (value.startsWith(RESOURCE_ID_PREFIX)) {
            return Color.parseColor(value);
        }
        if (!value.equalsIgnoreCase(COLOR_TRANSPARENT_COMPRESSED_HEX)) {
            return Color.parseColor(value);
        }
        return 0;
    }

    private static float parseFloatAttr(Context ctx, String value) {
        try {
            return ctx.getResources().getDimension(parseResId(ctx, value));
        } catch (IllegalArgumentException e) {
            try {
                return FigUtils.stringToDimension(ctx, value);
            } catch (Exception e2) {
                return Float.parseFloat(value);
            }
        }
    }

    private static String parseStringAttr(Context ctx, String value) {
        try {
            return ctx.getResources().getString(parseResId(ctx, value));
        } catch (IllegalArgumentException e) {
            return value;
        }
    }

    private static Method getMethodByName(Class clazz, String methodName) throws NoSuchMethodException {
        for (Method method : clazz.getMethods()) {
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException("No such public method (case insensitive): " + methodName + " in " + clazz);
    }

    private static Method getSetter(Class clazz, String fieldId) throws NoSuchMethodException {
        return getMethodByName(clazz, SETTER_PREFIX + fieldId);
    }

    private static Method getGetter(Class clazz, String fieldId) throws NoSuchMethodException {
        return getMethodByName(clazz, GETTER_PREFIX + fieldId);
    }

    static Object getObjectContaining(Object obj, String path) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (obj == null) {
            throw new NullPointerException("Attempt to call getObjectContaining(...) on a null Object instance.  Path was: " + path);
        }
        int separatorIndex = path.indexOf(DOT_SEPARATOR);
        if (separatorIndex <= 0) {
            return obj;
        }
        String lhs = path.substring(0, separatorIndex);
        String rhs = path.substring(separatorIndex + 1, path.length());
        Method m = getGetter(obj.getClass(), lhs);
        if (m != null) {
            return getObjectContaining(m.invoke(obj, new Object[0]), rhs);
        }
        throw new NoSuchMethodException("No getter found for field: " + lhs + " within " + obj.getClass());
    }

    private static Object[] inflateParams(Context ctx, Class[] params, String[] vals) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] out = new Object[params.length];
        int i = 0;
        for (Class param : params) {
            if (Enum.class.isAssignableFrom(param)) {
                out[i] = param.getMethod("valueOf", new Class[]{String.class}).invoke((Object) null, new Object[]{vals[i].toUpperCase()});
            } else if (param.equals(Float.TYPE) || param == Float.class) {
                out[i] = Float.valueOf(parseFloatAttr(ctx, vals[i]));
            } else if (param.equals(Integer.TYPE) || param == Integer.class) {
                out[i] = Integer.valueOf(parseIntAttr(vals[i]));
            } else if (param.equals(Boolean.TYPE) || param == Boolean.class) {
                out[i] = Boolean.valueOf(vals[i]);
            } else if (param.equals(String.class)) {
                out[i] = parseStringAttr(ctx, vals[i]);
            } else {
                throw new IllegalArgumentException("Error inflating XML: Setter requires param of unsupported type: " + param);
            }
            i++;
        }
        return out;
    }

    public static void configure(Context ctx, Object obj, File file) throws FigException {
        try {
            XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
            xppf.setNamespaceAware(true);
            XmlPullParser xpp = xppf.newPullParser();
            xpp.setInput(new FileInputStream(file), (String) null);
            configure(ctx, obj, xpp);
        } catch (FileNotFoundException e) {
            throw new FigException("Failed to open file for parsing", e);
        } catch (XmlPullParserException e2) {
            throw new FigException("Error while parsing file", e2);
        }
    }

    public static void configure(Context ctx, Object obj, HashMap<String, String> params) throws FigException {
        for (String key : params.keySet()) {
            configure(ctx, obj, key, params.get(key));
        }
    }

    private static void configure(Context ctx, Object obj, XmlPullParser xrp) throws FigException {
        try {
            HashMap<String, String> params = new HashMap<>();
            while (true) {
                if (xrp.getEventType() == 1) {
                    break;
                }
                xrp.next();
                String name = xrp.getName();
                if (xrp.getEventType() == 2) {
                    if (name.equalsIgnoreCase(CFG_ELEMENT_NAME)) {
                        for (int i = 0; i < xrp.getAttributeCount(); i++) {
                            params.put(xrp.getAttributeName(i), xrp.getAttributeValue(i));
                        }
                    }
                }
            }
            configure(ctx, obj, params);
        } catch (XmlPullParserException e) {
            throw new FigException("Error while parsing XML configuration", e);
        } catch (IOException e2) {
            throw new FigException("Error while parsing XML configuration", e2);
        }
    }

    public static void configure(Context ctx, Object obj, int xmlFileId) throws FigException {
        XmlResourceParser xrp = ctx.getResources().getXml(xmlFileId);
        try {
            configure(ctx, obj, (XmlPullParser) xrp);
        } finally {
            xrp.close();
        }
    }

    private static void configure(Context ctx, Object obj, String key, String value) throws FigException {
        String fieldId;
        try {
            Object o = getObjectContaining(obj, key);
            if (o != null) {
                int idx = key.lastIndexOf(DOT_SEPARATOR);
                if (idx > 0) {
                    fieldId = key.substring(idx + 1, key.length());
                } else {
                    fieldId = key;
                }
                Method m = getSetter(o.getClass(), fieldId);
                Class[] paramTypes = m.getParameterTypes();
                if (paramTypes.length >= 1) {
                    String[] paramStrs = value.split("\\|");
                    if (paramStrs.length == paramTypes.length) {
                        m.invoke(o, inflateParams(ctx, paramTypes, paramStrs));
                        return;
                    }
                    throw new IllegalArgumentException("Error inflating XML: Unexpected number of argments passed to \"" + m.getName() + "\".  Expected: " + paramTypes.length + " Got: " + paramStrs.length);
                }
                throw new IllegalArgumentException("Error inflating XML: no setter method found for param \"" + fieldId + "\".");
            }
        } catch (IllegalAccessException e) {
            throw new FigException("Error while parsing key: " + key + " value: " + value, e);
        } catch (InvocationTargetException e2) {
            throw new FigException("Error while parsing key: " + key + " value: " + value, e2);
        } catch (NoSuchMethodException e3) {
            throw new FigException("Error while parsing key: " + key + " value: " + value, e3);
        }
    }
}
