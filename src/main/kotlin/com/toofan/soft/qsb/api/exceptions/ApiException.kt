package com.toofan.soft.qsb.api.exceptions

import com.toofan.soft.qsb.api.session.Auth
import com.toofan.soft.qsb.api.session.Language

enum class ApiException(
    private val arabicMessage: String,
    private val englishMessage: String,
    private val code: String
) {
//    SUCCESS("نجاح", "Success", "2__"),
//    OK("تم بنجاح", "OK", "200"),
//    CREATED("تم الإنشاء", "Created", "201"),
//    ACCEPTED("تم القبول", "Accepted", "202"),
//    NON_AUTHORITATIVE_INFORMATION("معلومات غير موثوقة", "Non-Authoritative Information", "203"),
//    NO_CONTENT("لا يوجد محتوى", "No Content", "204"),
//    RESET_CONTENT("إعادة تعيين المحتوى", "Reset Content", "205"),
//    PARTIAL_CONTENT("محتوى جزئي", "Partial Content", "206"),

    REDIRECTION_ERROR("إعادة توجيه!", "Redirection!", "3__"),
    MULTIPLE_CHOICES("خيارات متعددة!", "Multiple Choices!", "300"),
    MOVED_PERMANENTLY("نقل دائم!", "Moved Permanently!", "301"),
    FOUND("تم العثور!", "Found!", "302"),
    SEE_OTHER("انظر الآخر!", "See Other!", "303"),
    NOT_MODIFIED("لم يتم التعديل!", "Not Modified!", "304"),
    USE_PROXY("استخدم وكيل!", "Use Proxy!", "305"),
    TEMPORARY_REDIRECT("إعادة توجيه مؤقتة!", "Temporary Redirect!", "307"),
    PERMANENT_REDIRECT("إعادة توجيه دائمة!", "Permanent Redirect!", "308"),

    CLIENT_ERROR("خطأ غير معروف!", "Unknown Error!", "4__"),
    INVALID_INPUTS("مدخلات غير صحيحة!", "Invalid Inputs!", "400"),
    UNAUTHORIZED("لا يوجد تصريح!", "Unauthorized!", "401"),
    FORBIDDEN("ممنوع!", "Forbidden!", "403"),
    NOT_FOUND("لم يتم العثور على الصفحة!", "Not Found!", "404"),
    METHOD_NOT_ALLOWED("الطريقة غير مسموح بها!", "Method Not Allowed!", "405"),
    CONFLICT("تعارض!", "Conflict!", "409"),

    SERVER_ERROR("خطأ غير معروف!", "Unknown Error!", "5__"),
    INTERNAL_SERVER_ERROR("خطأ في الخادم الداخلي!", "Internal Server Error!", "500"),
    NOT_IMPLEMENTED("لم يتم التنفيذ!", "Not Implemented!", "501"),
    BAD_GATEWAY("بوابة غير صالحة!", "Bad Gateway!", "502"),
    SERVICE_UNAVAILABLE("الخدمة غير متوفرة!", "Service Unavailable!", "503"),
    GATEWAY_TIMEOUT("انتهى وقت البوابة!", "Gateway Timeout!", "504"),

    UNKNOWN_ERROR("خطأ غير معروف!", "Unknown Error!", "___");

    val message get() = this.code + " - " + when (Auth.language) {
        Language.ARABIC -> this.arabicMessage
        Language.ENGLISH -> this.englishMessage
        null -> this.arabicMessage
    }

    companion object {
        fun of(code: Int): ApiException {
            return when (code) {
//                in 200..299 -> values().find { it.code == code.toString() } ?: SUCCESS
                in 300..399 -> values().find { it.code == code.toString() } ?: REDIRECTION_ERROR
                in 400..499 -> values().find { it.code == code.toString() } ?: CLIENT_ERROR
                in 500..599 -> values().find { it.code == code.toString() } ?: SERVER_ERROR
                else -> UNKNOWN_ERROR
            }
        }
    }
}