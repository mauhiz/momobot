CREATE TABLE IF NOT EXISTS `wars` (
  `nbjoueurs` tinyint(4) NOT NULL,
  `serv` tinyint(4) NOT NULL,
  `lvl` tinyint(4) NOT NULL,
  `msg` text NOT NULL,
  `user` tinytext NOT NULL,
  `datetime` datetime NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
