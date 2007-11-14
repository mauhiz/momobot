package ircbot.trigger;

import ircbot.IrcUser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public class ActivateTrigger extends AbstractTrigger implements IAdminTrigger, IPriveTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(ActivateTrigger.class);

    /**
     * @param trigger
     */
    public ActivateTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @param from
     * @param className
     */
    private void dispClassError(final IrcUser from, final String className) {
        MomoBot.getBotInstance().sendMessage(from, className + ": is not a valid trigger class name.");
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(ircbot.IrcUser, java.lang.String)
     */
    public void executePrivateTrigger(final IrcUser from, final String message) {
        final String[] args = StringUtils.split(message, " ");
        if (args == null || args.length < 3) {
            MomoBot.getBotInstance().sendMessage(from,
                    "this trigger needs at least two parameters : class name and trigger name");
        } else {
            String className = args[1];
            LOG.info("Toggle class = " + className);
            if (!className.endsWith("Trigger")) {
                dispClassError(from, className);
            } else {
                try {
                    Class trigClass = Class.forName(className);
                    Constructor cons = trigClass.getConstructor(String.class);
                    for (int i = 2; i < args.length; ++i) {
                        LOG.info("Toggle text = " + args[i]);
                        try {
                            cons.newInstance(args[i]);
                        } catch (InstantiationException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn(e, e);
                            }
                        } catch (IllegalAccessException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn(e, e);
                            }
                        } catch (InvocationTargetException e) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn(e, e);
                            }
                        }
                    }
                } catch (ClassNotFoundException cnfe) {
                    LOG.warn(cnfe, cnfe);
                    dispClassError(from, className);
                } catch (NoSuchMethodException e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn(e, e);
                    }
                }
            }
        }
    }
}
