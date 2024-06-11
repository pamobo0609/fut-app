package com.fut.app.common

const val EMPTY_STRING = ""
const val SLASH = "/"
const val SPACE = " "
const val ZERO = 0

const val EMAIL_REGEX_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"