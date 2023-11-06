package org.example;

import org.json.JSONObject;

interface Rule {
    boolean match(JSONObject data);
    String message();
}



