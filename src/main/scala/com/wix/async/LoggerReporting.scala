package com.wix.async

import org.slf4j.LoggerFactory
import com.twitter.util.Duration
import com.wix.async.FuturePerfect._

/**
 * @author shaiyallin
 * @since 12/6/13
 */

trait LoggerReporting { this: Reporting[Event] =>

  private val log = LoggerFactory.getLogger(getClass)

  listenFor {
   case Retrying(timeout, remainingRetries, executionName) => log.warn(s"Execution [$executionName] timed out after ${timeout.inMillis} ms, retrying $remainingRetries more times.")
   case GaveUp(timeout, _, executionName) => log.error(s"Execution [$executionName] timed out after ${timeout.inMillis} ms, giving up.")
   case ExceededTimeout(actual, executionName) => log.error(s"Execution [$executionName] timed out, actual duration was ${actual.inMillis} ms.")
   case TimeSpentInQueue(time, executionName) => log.info(s"Execution [$executionName] started after spending ${time.inMillis} ms in queue.")
   case Successful(time, executionName) => log.info(s"Execution [$executionName] succeeded after ${time.inMillis} ms.")
   case Failed(time, error, executionName) => log.error(s"Execution [$executionName] failed after ${time.inMillis} ms.", error)
   case TimeoutWhileInQueue(time, error, executionName) => log.error(s"Execution [$executionName] timed out after waiting ${time.inMillis} in queue.", error)
  }
}