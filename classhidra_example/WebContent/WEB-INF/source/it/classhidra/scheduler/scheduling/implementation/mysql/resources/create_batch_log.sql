CREATE TABLE `batch_log` (
  `cd_btch` varchar(50) NOT NULL,
  `tm_start` datetime NOT NULL,
  `tm_fin` datetime NOT NULL,
  `st_exec` int(10) unsigned NOT NULL,
  `dsc_exec` varchar(1000) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1; 