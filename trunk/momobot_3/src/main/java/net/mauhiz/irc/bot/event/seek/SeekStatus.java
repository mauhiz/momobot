package net.mauhiz.irc.bot.event.seek;

public enum SeekStatus {
    ASKED_IP(6), ASKED_IP2(95), ASKED_MATCH_LVL(67), GOT_ANSWER(2), GOT_ANSWER_SRV_OFF(5), GOT_IP(100), GOT_MATCH_LVL(3), GOT_MATCH_LVL_ANSWER(
            65), GOT_MATCH_LVL_SRV_OFF(4), SENT_MSG(66), STARTING(1), UNK_96(96), UNK_98(98);

    private SeekStatus(int id) {

    }
}
