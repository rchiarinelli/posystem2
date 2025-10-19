package br.com.brainyit.posystem2.domain;

/**
 * CRITICAL > HIGH > STANDARD > LOW
 * <br/>
 * CRITICAL = Takes precedence over all other priorities
 * HIGH = Takes precedence over STANDARD and LOW
 * STANDARD = Takes precedence over LOW
 * LOW = Lowest priority
 * <br/>
 * CRITICAL means that the production order must stop all other production
 * orders with same priority and lower priority.
 * <br/>
 * HIGH means that the production order must can share 75% of time with
 * other production orders with lower priority.
 * <br/>
 * STANDARD means that the production order can share 50% of time with other
 * production orders with same priority and 75% of time with other
 * production orders with lower priority.
 * <br/>
 * LOW means that the production order can share 25% of time with other
 * production orders with same priority.
 */
public enum POPriority {

    LOW, STANDARD, HIGH, CRITICAL;

}
