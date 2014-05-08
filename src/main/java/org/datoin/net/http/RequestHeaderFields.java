package org.datoin.net.http;

/** Allowed HTTP Request headers as per : http://www.w3.org/Protocols/HTTP/HTRQ_Headers.html
 * Author : umarshah@simplyphi.com
 * Created on : 7/5/14.
 */

public enum RequestHeaderFields {
    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    AUTHORIZATION("Authorization"),
    CHARGE_TO("Charge-To"),
    IF_MODIFIED_SINCE("If-Modified-Since"),
    PRAGMA("Pragma"),
    USER_AGENT("User-Agent"),
    REFERER("Referer");

    private String headerField;

    RequestHeaderFields(String headerField) {

        this.headerField = headerField;
    }

    public String getName() {
        return headerField;
    }
}
