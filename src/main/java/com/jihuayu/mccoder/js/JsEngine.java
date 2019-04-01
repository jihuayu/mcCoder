package com.jihuayu.mccoder.js;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;

class JsEngine {
    private static final NashornScriptEngineFactory nashornScriptEngineFactory = new NashornScriptEngineFactory();
    static final ScriptEngine engine = nashornScriptEngineFactory.getScriptEngine();
}
