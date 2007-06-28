package ircbot;

/**
 * This interface contains the values of all numeric replies specified in section 6 of RFC 1459. Refer to RFC 1459 for
 * further information.
 * <p>
 * If you override the onServerResponse method in the PircBot class, you may find these constants useful when comparing
 * the numeric value of a given code.
 * @author Paul James Mutton, <a href="http://www.jibble.org/">http://www.jibble.org/</a>
 */
interface IIrcConstants {
    /**
     * .
     */
    short ERR_ALREADYREGISTRED = 462;
    /**
     * .
     */
    short ERR_BADCHANMASK      = 476;
    /**
     * .
     */
    short ERR_BADCHANNELKEY    = 475;
    /**
     * .
     */
    short ERR_BANNEDFROMCHAN   = 474;
    /**
     * .
     */
    short ERR_CANNOTSENDTOCHAN = 404;
    /**
     * .
     */
    short ERR_CANTKILLSERVER   = 483;
    /**
     * .
     */
    short ERR_CHANNELISFULL    = 471;
    /**
     * .
     */
    short ERR_CHANOPRIVSNEEDED = 482;
    /**
     * .
     */
    short ERR_ERRONEUSNICKNAME = 432;
    /**
     * .
     */
    short ERR_FILEERROR        = 424;
    /**
     * .
     */
    short ERR_INVITEONLYCHAN   = 473;
    /**
     * .
     */
    short ERR_KEYSET           = 467;
    /**
     * .
     */
    short ERR_NEEDMOREPARAMS   = 461;
    /**
     * .
     */
    short ERR_NICKCOLLISION    = 436;
    /**
     * .
     */
    short ERR_NICKNAMEINUSE    = 433;
    /**
     * .
     */
    short ERR_NOADMININFO      = 423;
    /**
     * .
     */
    short ERR_NOLOGIN          = 444;
    /**
     * .
     */
    short ERR_NOMOTD           = 422;
    /**
     * .
     */
    short ERR_NONICKNAMEGIVEN  = 431;
    /**
     * .
     */
    short ERR_NOOPERHOST       = 491;
    /**
     * .
     */
    short ERR_NOORIGIN         = 409;
    /**
     * .
     */
    short ERR_NOPERMFORHOST    = 463;
    /**
     * .
     */
    short ERR_NOPRIVILEGES     = 481;
    /**
     * .
     */
    short ERR_NORECIPIENT      = 411;
    /**
     * .
     */
    short ERR_NOSERVICEHOST    = 492;
    /**
     * .
     */
    short ERR_NOSUCHCHANNEL    = 403;
    /**
     * .
     */
    short ERR_NOSUCHNICK       = 401;
    /**
     * .
     */
    short ERR_NOSUCHSERVER     = 402;
    /**
     * .
     */
    short ERR_NOTEXTTOSEND     = 412;
    /**
     * .
     */
    short ERR_NOTONCHANNEL     = 442;
    /**
     * .
     */
    short ERR_NOTOPLEVEL       = 413;
    /**
     * .
     */
    short ERR_NOTREGISTERED    = 451;
    /**
     * .
     */
    short ERR_PASSWDMISMATCH   = 464;
    /**
     * .
     */
    short ERR_SUMMONDISABLED   = 445;
    /**
     * .
     */
    short ERR_TOOMANYCHANNELS  = 405;
    /**
     * .
     */
    short ERR_TOOMANYTARGETS   = 407;
    /**
     * .
     */
    short ERR_UMODEUNKNOWNFLAG = 501;
    /**
     * .
     */
    short ERR_UNKNOWNCOMMAND   = 421;
    /**
     * .
     */
    short ERR_UNKNOWNMODE      = 472;
    /**
     * .
     */
    short ERR_USERNOTINCHANNEL = 441;
    /**
     * .
     */
    short ERR_USERONCHANNEL    = 443;
    /**
     * .
     */
    short ERR_USERSDISABLED    = 446;
    /**
     * .
     */
    short ERR_USERSDONTMATCH   = 502;
    /**
     * .
     */
    short ERR_WASNOSUCHNICK    = 406;
    /**
     * .
     */
    short ERR_WILDTOPLEVEL     = 414;
    /**
     * .
     */
    short ERR_YOUREBANNEDCREEP = 465;
    /**
     * .
     */
    short ERR_YOUWILLBEBANNED  = 466;
    /**
     * .
     */
    short RPL_ADMINEMAIL       = 259;
    /**
     * .
     */
    short RPL_ADMINLOC1        = 257;
    /**
     * .
     */
    short RPL_ADMINLOC2        = 258;
    /**
     * .
     */
    short RPL_ADMINME          = 256;
    /**
     * .
     */
    short RPL_AWAY             = 301;
    /**
     * .
     */
    short RPL_BANLIST          = 367;
    /**
     * .
     */
    short RPL_CHANNELMODEIS    = 324;
    /**
     * .
     */
    short RPL_CLOSEEND         = 363;
    /**
     * .
     */
    short RPL_CLOSING          = 362;
    /**
     * .
     */
    short RPL_ENDOFBANLIST     = 368;
    /**
     * .
     */
    short RPL_ENDOFINFO        = 374;
    /**
     * .
     */
    short RPL_ENDOFLINKS       = 365;
    /**
     * .
     */
    short RPL_ENDOFMOTD        = 376;
    /**
     * .
     */
    short RPL_ENDOFNAMES       = 366;
    /**
     * .
     */
    short RPL_ENDOFSERVICES    = 232;
    /**
     * .
     */
    short RPL_ENDOFSTATS       = 219;
    /**
     * .
     */
    short RPL_ENDOFUSERS       = 394;
    /**
     * .
     */
    short RPL_ENDOFWHO         = 315;
    /**
     * .
     */
    short RPL_ENDOFWHOIS       = 318;
    /**
     * .
     */
    short RPL_ENDOFWHOWAS      = 369;
    /**
     * .
     */
    short RPL_INFO             = 371;
    /**
     * .
     */
    short RPL_INFOSTART        = 373;
    /**
     * .
     */
    short RPL_INVITING         = 341;
    /**
     * .
     */
    short RPL_ISON             = 303;
    /**
     * .
     */
    short RPL_KILLDONE         = 361;
    /**
     * .
     */
    short RPL_LINKS            = 364;
    /**
     * .
     */
    short RPL_LIST             = 322;
    /**
     * .
     */
    short RPL_LISTEND          = 323;
    /**
     * .
     */
    short RPL_LISTSTART        = 321;
    /**
     * .
     */
    short RPL_LUSERCHANNELS    = 254;
    /**
     * .
     */
    short RPL_LUSERCLIENT      = 251;
    /**
     * .
     */
    short RPL_LUSERME          = 255;
    /**
     * .
     */
    short RPL_LUSEROP          = 252;
    /**
     * ..
     */
    short RPL_LUSERUNKNOWN     = 253;
    /**
     * ..
     */
    short RPL_MOTD             = 372;
    /**
     * ..
     */
    short RPL_MOTDSTART        = 375;
    /**
     * .
     */
    short RPL_MYPORTIS         = 384;
    /**
     * .
     */
    short RPL_NAMREPLY         = 353;
    /**
     * .
     */
    short RPL_NONE             = 300;
    /**
     * .
     */
    short RPL_NOTOPIC          = 331;
    /**
     * .
     */
    short RPL_NOUSERS          = 395;
    /**
     * .
     */
    short RPL_NOWAWAY          = 306;
    /**
     * .
     */
    short RPL_REHASHING        = 382;
    /**
     * .
     */
    short RPL_SERVICE          = 233;
    /**
     * .
     */
    short RPL_SERVICEINFO      = 231;
    /**
     * .
     */
    short RPL_SERVLIST         = 234;
    /**
     * .
     */
    short RPL_SERVLISTEND      = 235;
    /**
     * .
     */
    short RPL_STATSCLINE       = 213;
    /**
     * .
     */
    short RPL_STATSCOMMANDS    = 212;
    /**
     * .
     */
    short RPL_STATSHLINE       = 244;
    /**
     * .
     */
    short RPL_STATSILINE       = 215;
    /**
     * .
     */
    short RPL_STATSKLINE       = 216;
    /**
     * .
     */
    short RPL_STATSLINKINFO    = 211;
    /**
     * .
     */
    short RPL_STATSLLINE       = 241;
    /**
     * .
     */
    short RPL_STATSNLINE       = 214;
    /**
     * .
     */
    short RPL_STATSOLINE       = 243;
    /**
     * .
     */
    short RPL_STATSQLINE       = 217;
    /**
     * .
     */
    short RPL_STATSUPTIME      = 242;
    /**
     * .
     */
    short RPL_STATSYLINE       = 218;
    /**
     * .
     */
    short RPL_SUMMONING        = 342;
    /**
     * .
     */
    short RPL_TIME             = 391;
    /**
     * .
     */
    short RPL_TOPIC            = 332;
    /**
     * .
     */
    short RPL_TOPICINFO        = 333;
    /**
     * .
     */
    short RPL_TRACECLASS       = 209;
    /**
     * .
     */
    short RPL_TRACECONNECTING  = 201;
    /**
     * .
     */
    short RPL_TRACEHANDSHAKE   = 202;
    /**
     * .
     */
    short RPL_TRACELINK        = 200;
    /**
     * .
     */
    short RPL_TRACELOG         = 261;
    /**
     * .
     */
    short RPL_TRACENEWTYPE     = 208;
    /**
     * .
     */
    short RPL_TRACEOPERATOR    = 204;
    /**
     * .
     */
    short RPL_TRACESERVER      = 206;
    /**
     * .
     */
    short RPL_TRACEUNKNOWN     = 203;
    /**
     * .
     */
    short RPL_TRACEUSER        = 205;
    /**
     * .
     */
    short RPL_UMODEIS          = 221;
    /**
     * .
     */
    short RPL_UNAWAY           = 305;
    /**
     * .
     */
    short RPL_USERHOST         = 302;
    /**
     * .
     */
    short RPL_USERS            = 393;
    /**
     * .
     */
    short RPL_USERSSTART       = 392;
    /**
     * .
     */
    short RPL_VERSION          = 351;
    /**
     * .auth Qnet.
     */
    short RPL_WHOISAUTH        = 330;
    /**
     * .
     */
    short RPL_WHOISCHANNELS    = 319;
    /**
     * .
     */
    short RPL_WHOISCHANOP      = 316;
    /**
     * .
     */
    short RPL_WHOISIDLE        = 317;
    /**
     * .
     */
    short RPL_WHOISOPERATOR    = 313;
    /**
     * .
     */
    short RPL_WHOISSERVER      = 312;
    /**
     * .
     */
    short RPL_WHOISUSER        = 311;
    /**
     * .
     */
    short RPL_WHOREPLY         = 352;
    /**
     * .
     */
    short RPL_WHOWASUSER       = 314;
    /**
     * .
     */
    short RPL_YOUREOPER        = 381;
}
