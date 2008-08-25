CREATE TABLE IF NOT EXISTS `wars` (
  `nbjoueurs` tinyint(4) NOT NULL,
  `serv` tinyint(4) NOT NULL,
  `lvl` tinyint(4) NOT NULL,
  `msg` tinytext NOT NULL,
  `user` tinytext NOT NULL,
  `when` datetime NOT NULL,
  PRIMARY KEY  (`user`(32))
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `untreated` (
  `id` int(11) NOT NULL auto_increment,
  `when` datetime NOT NULL,
  `user` tinytext NOT NULL,
  `msg` tinytext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
