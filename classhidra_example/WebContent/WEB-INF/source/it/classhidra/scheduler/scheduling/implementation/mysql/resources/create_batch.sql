CREATE TABLE `batch` (
  `cd_ist` smallint(6) NOT NULL,
  `cd_btch` varchar(50) NOT NULL,
  `cd_p_btch` varchar(50) default NULL,
  `cls_btch` varchar(500) default NULL,
  `dsc_btch` varchar(100) NOT NULL,
  `ord` int(10) unsigned NOT NULL,
  `period` varchar(50) default NULL,
  `tm_next` datetime default NULL,
  `state` int(10) default NULL,
  `st_exec` int(10) unsigned default NULL,
  PRIMARY KEY  USING BTREE (`cd_btch`,`cd_ist`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
 