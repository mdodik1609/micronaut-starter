package hr.mdodik.util

import org.slf4j.LoggerFactory

abstract class HasLog {
    protected open val log = LoggerFactory.getLogger(javaClass)!!
}