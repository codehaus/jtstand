/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jtstand.visa

import com.sun.jna.NativeLibrary
import com.sun.jna.Platform
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Native

public class Visa {
    /*- Attributes --------------------------------------------------------------*/
    static final int VI_ATTR_RSRC_CLASS = (0xBFFF0001);
    static final int VI_ATTR_RSRC_NAME = (0xBFFF0002);
    static final int VI_ATTR_RSRC_IMPL_VERSION = (0x3FFF0003);
    static final int VI_ATTR_RSRC_LOCK_STATE = (0x3FFF0004);
    static final int VI_ATTR_MAX_QUEUE_LENGTH = (0x3FFF0005);
    static final int VI_ATTR_USER_DATA = (0x3FFF0007);
    static final int VI_ATTR_FDC_CHNL = (0x3FFF000D);
    static final int VI_ATTR_FDC_MODE = (0x3FFF000F);
    static final int VI_ATTR_FDC_GEN_SIGNAL_EN = (0x3FFF0011);
    static final int VI_ATTR_FDC_USE_PAIR = (0x3FFF0013);
    static final int VI_ATTR_SEND_END_EN = (0x3FFF0016);
    static final int VI_ATTR_TERMCHAR = (0x3FFF0018);
    static final int VI_ATTR_TMO_VALUE = (0x3FFF001A);
    static final int VI_ATTR_GPIB_READDR_EN = (0x3FFF001B);
    static final int VI_ATTR_IO_PROT = (0x3FFF001C);
    static final int VI_ATTR_DMA_ALLOW_EN = (0x3FFF001E);
    static final int VI_ATTR_ASRL_BAUD = (0x3FFF0021);
    static final int VI_ATTR_ASRL_DATA_BITS = (0x3FFF0022);
    static final int VI_ATTR_ASRL_PARITY = (0x3FFF0023);
    static final int VI_ATTR_ASRL_STOP_BITS = (0x3FFF0024);
    static final int VI_ATTR_ASRL_FLOW_CNTRL = (0x3FFF0025);
    static final int VI_ATTR_RD_BUF_OPER_MODE = (0x3FFF002A);
    static final int VI_ATTR_RD_BUF_SIZE = (0x3FFF002B);
    static final int VI_ATTR_WR_BUF_OPER_MODE = (0x3FFF002D);
    static final int VI_ATTR_WR_BUF_SIZE = (0x3FFF002E);
    static final int VI_ATTR_SUPPRESS_END_EN = (0x3FFF0036);
    static final int VI_ATTR_TERMCHAR_EN = (0x3FFF0038);
    static final int VI_ATTR_DEST_ACCESS_PRIV = (0x3FFF0039);
    static final int VI_ATTR_DEST_BYTE_ORDER = (0x3FFF003A);
    static final int VI_ATTR_SRC_ACCESS_PRIV = (0x3FFF003C);
    static final int VI_ATTR_SRC_BYTE_ORDER = (0x3FFF003D);
    static final int VI_ATTR_SRC_INCREMENT = (0x3FFF0040);
    static final int VI_ATTR_DEST_INCREMENT = (0x3FFF0041);
    static final int VI_ATTR_WIN_ACCESS_PRIV = (0x3FFF0045);
    static final int VI_ATTR_WIN_BYTE_ORDER = (0x3FFF0047);
    static final int VI_ATTR_GPIB_ATN_STATE = (0x3FFF0057);
    static final int VI_ATTR_GPIB_ADDR_STATE = (0x3FFF005C);
    static final int VI_ATTR_GPIB_CIC_STATE = (0x3FFF005E);
    static final int VI_ATTR_GPIB_NDAC_STATE = (0x3FFF0062);
    static final int VI_ATTR_GPIB_SRQ_STATE = (0x3FFF0067);
    static final int VI_ATTR_GPIB_SYS_CNTRL_STATE = (0x3FFF0068);
    static final int VI_ATTR_GPIB_HS488_CBL_LEN = (0x3FFF0069);
    static final int VI_ATTR_CMDR_LA = (0x3FFF006B);
    static final int VI_ATTR_VXI_DEV_CLASS = (0x3FFF006C);
    static final int VI_ATTR_MAINFRAME_LA = (0x3FFF0070);
    static final int VI_ATTR_MANF_NAME = (0xBFFF0072);
    static final int VI_ATTR_MODEL_NAME = (0xBFFF0077);
    static final int VI_ATTR_VXI_VME_INTR_STATUS = (0x3FFF008B);
    static final int VI_ATTR_VXI_TRIG_STATUS = (0x3FFF008D);
    static final int VI_ATTR_VXI_VME_SYSFAIL_STATE = (0x3FFF0094);
    static final int VI_ATTR_WIN_BASE_ADDR = (0x3FFF0098);
    static final int VI_ATTR_WIN_SIZE = (0x3FFF009A);
    static final int VI_ATTR_ASRL_AVAIL_NUM = (0x3FFF00AC);
    static final int VI_ATTR_MEM_BASE = (0x3FFF00AD);
    static final int VI_ATTR_ASRL_CTS_STATE = (0x3FFF00AE);
    static final int VI_ATTR_ASRL_DCD_STATE = (0x3FFF00AF);
    static final int VI_ATTR_ASRL_DSR_STATE = (0x3FFF00B1);
    static final int VI_ATTR_ASRL_DTR_STATE = (0x3FFF00B2);
    static final int VI_ATTR_ASRL_END_IN = (0x3FFF00B3);
    static final int VI_ATTR_ASRL_END_OUT = (0x3FFF00B4);
    static final int VI_ATTR_ASRL_REPLACE_CHAR = (0x3FFF00BE);
    static final int VI_ATTR_ASRL_RI_STATE = (0x3FFF00BF);
    static final int VI_ATTR_ASRL_RTS_STATE = (0x3FFF00C0);
    static final int VI_ATTR_ASRL_XON_CHAR = (0x3FFF00C1);
    static final int VI_ATTR_ASRL_XOFF_CHAR = (0x3FFF00C2);
    static final int VI_ATTR_WIN_ACCESS = (0x3FFF00C3);
    static final int VI_ATTR_RM_SESSION = (0x3FFF00C4);
    static final int VI_ATTR_VXI_LA = (0x3FFF00D5);
    static final int VI_ATTR_MANF_ID = (0x3FFF00D9);
    static final int VI_ATTR_MEM_SIZE = (0x3FFF00DD);
    static final int VI_ATTR_MEM_SPACE = (0x3FFF00DE);
    static final int VI_ATTR_MODEL_CODE = (0x3FFF00DF);
    static final int VI_ATTR_SLOT = (0x3FFF00E8);
    static final int VI_ATTR_INTF_INST_NAME = (0xBFFF00E9);
    static final int VI_ATTR_IMMEDIATE_SERV = (0x3FFF0100);
    static final int VI_ATTR_INTF_PARENT_NUM = (0x3FFF0101);
    static final int VI_ATTR_RSRC_SPEC_VERSION = (0x3FFF0170);
    static final int VI_ATTR_INTF_TYPE = (0x3FFF0171);
    static final int VI_ATTR_GPIB_PRIMARY_ADDR = (0x3FFF0172);
    static final int VI_ATTR_GPIB_SECONDARY_ADDR = (0x3FFF0173);
    static final int VI_ATTR_RSRC_MANF_NAME = (0xBFFF0174);
    static final int VI_ATTR_RSRC_MANF_ID = (0x3FFF0175);
    static final int VI_ATTR_INTF_NUM = (0x3FFF0176);
    static final int VI_ATTR_TRIG_ID = (0x3FFF0177);
    static final int VI_ATTR_GPIB_REN_STATE = (0x3FFF0181);
    static final int VI_ATTR_GPIB_UNADDR_EN = (0x3FFF0184);
    static final int VI_ATTR_DEV_STATUS_BYTE = (0x3FFF0189);
    static final int VI_ATTR_FILE_APPEND_EN = (0x3FFF0192);
    static final int VI_ATTR_VXI_TRIG_SUPPORT = (0x3FFF0194);
    static final int VI_ATTR_TCPIP_ADDR = (0xBFFF0195);
    static final int VI_ATTR_TCPIP_HOSTNAME = (0xBFFF0196);
    static final int VI_ATTR_TCPIP_PORT = (0x3FFF0197);
    static final int VI_ATTR_TCPIP_DEVICE_NAME = (0xBFFF0199);
    static final int VI_ATTR_TCPIP_NODELAY = (0x3FFF019A);
    static final int VI_ATTR_TCPIP_KEEPALIVE = (0x3FFF019B);
    static final int VI_ATTR_4882_COMPLIANT = (0x3FFF019F);
    static final int VI_ATTR_USB_SERIAL_NUM = (0xBFFF01A0);
    static final int VI_ATTR_USB_INTFC_NUM = (0x3FFF01A1);
    static final int VI_ATTR_USB_PROTOCOL = (0x3FFF01A7);
    static final int VI_ATTR_USB_MAX_INTR_SIZE = (0x3FFF01AF);
    static final int VI_ATTR_JOB_ID = (0x3FFF4006);
    static final int VI_ATTR_EVENT_TYPE = (0x3FFF4010);
    static final int VI_ATTR_SIGP_STATUS_ID = (0x3FFF4011);
    static final int VI_ATTR_RECV_TRIG_ID = (0x3FFF4012);
    static final int VI_ATTR_INTR_STATUS_ID = (0x3FFF4023);
    static final int VI_ATTR_STATUS = (0x3FFF4025);
    static final int VI_ATTR_RET_COUNT = (0x3FFF4026);
    static final int VI_ATTR_BUFFER = (0x3FFF4027);
    static final int VI_ATTR_RECV_INTR_LEVEL = (0x3FFF4041);
    static final int VI_ATTR_OPER_NAME = (0xBFFF4042);
    static final int VI_ATTR_GPIB_RECV_CIC_STATE = (0x3FFF4193);
    static final int VI_ATTR_RECV_TCPIP_ADDR = (0xBFFF4198);
    static final int VI_ATTR_USB_RECV_INTR_SIZE = (0x3FFF41B0);
    static final int VI_ATTR_USB_RECV_INTR_DATA = (0xBFFF41B1);
    /*- Event Types -------------------------------------------------------------*/
    static final int VI_EVENT_IO_COMPLETION = (0x3FFF2009);
    static final int VI_EVENT_TRIG = (0xBFFF200A);
    static final int VI_EVENT_SERVICE_REQ = (0x3FFF200B);
    static final int VI_EVENT_CLEAR = (0x3FFF200D);
    static final int VI_EVENT_EXCEPTION = (0xBFFF200E);
    static final int VI_EVENT_GPIB_CIC = (0x3FFF2012);
    static final int VI_EVENT_GPIB_TALK = (0x3FFF2013);
    static final int VI_EVENT_GPIB_LISTEN = (0x3FFF2014);
    static final int VI_EVENT_VXI_VME_SYSFAIL = (0x3FFF201D);
    static final int VI_EVENT_VXI_VME_SYSRESET = (0x3FFF201E);
    static final int VI_EVENT_VXI_SIGP = (0x3FFF2020);
    static final int VI_EVENT_VXI_VME_INTR = (0xBFFF2021);
    static final int VI_EVENT_TCPIP_CONNECT = (0x3FFF2036);
    static final int VI_EVENT_USB_INTR = (0x3FFF2037);
    static final int VI_ALL_ENABLED_EVENTS = (0x3FFF7FFF);
    /*- Completion and Error Codes ----------------------------------------------*/
    static final int VI_SUCCESS_EVENT_EN = (0x3FFF0002); /* 3FFF0002,  1073676290 */
    static final int VI_SUCCESS_EVENT_DIS = (0x3FFF0003); /* 3FFF0003,  1073676291 */
    static final int VI_SUCCESS_QUEUE_EMPTY = (0x3FFF0004); /* 3FFF0004,  1073676292 */
    static final int VI_SUCCESS_TERM_CHAR = (0x3FFF0005); /* 3FFF0005,  1073676293 */
    static final int VI_SUCCESS_MAX_CNT = (0x3FFF0006); /* 3FFF0006,  1073676294 */
    static final int VI_SUCCESS_DEV_NPRESENT = (0x3FFF007D); /* 3FFF007D,  1073676413 */
    static final int VI_SUCCESS_TRIG_MAPPED = (0x3FFF007E); /* 3FFF007E,  1073676414 */
    static final int VI_SUCCESS_QUEUE_NEMPTY = (0x3FFF0080); /* 3FFF0080,  1073676416 */
    static final int VI_SUCCESS_NCHAIN = (0x3FFF0098); /* 3FFF0098,  1073676440 */
    static final int VI_SUCCESS_NESTED_SHARED = (0x3FFF0099); /* 3FFF0099,  1073676441 */
    static final int VI_SUCCESS_NESTED_EXCLUSIVE = (0x3FFF009A); /* 3FFF009A,  1073676442 */
    static final int VI_SUCCESS_SYNC = (0x3FFF009B); /* 3FFF009B,  1073676443 */
    static final int VI_WARN_QUEUE_OVERFLOW = (0x3FFF000C); /* 3FFF000C,  1073676300 */
    static final int VI_WARN_CONFIG_NLOADED = (0x3FFF0077); /* 3FFF0077,  1073676407 */
    static final int VI_WARN_NULL_OBJECT = (0x3FFF0082); /* 3FFF0082,  1073676418 */
    static final int VI_WARN_NSUP_ATTR_STATE = (0x3FFF0084); /* 3FFF0084,  1073676420 */
    static final int VI_WARN_UNKNOWN_STATUS = (0x3FFF0085); /* 3FFF0085,  1073676421 */
    static final int VI_WARN_NSUP_BUF = (0x3FFF0088); /* 3FFF0088,  1073676424 */
    static final int VI_WARN_EXT_FUNC_NIMPL = (0x3FFF00A9); /* 3FFF00A9,  1073676457 */
    static final int _VI_ERROR = 0x80000000;
    static final int VI_ERROR_SYSTEM_ERROR = (_VI_ERROR + 0x3FFF0000); /* BFFF0000, -1073807360 */
    static final int VI_ERROR_INV_OBJECT = (_VI_ERROR + 0x3FFF000E); /* BFFF000E, -1073807346 */
    static final int VI_ERROR_RSRC_LOCKED = (_VI_ERROR + 0x3FFF000F); /* BFFF000F, -1073807345 */
    static final int VI_ERROR_INV_EXPR = (_VI_ERROR + 0x3FFF0010); /* BFFF0010, -1073807344 */
    static final int VI_ERROR_RSRC_NFOUND = (_VI_ERROR + 0x3FFF0011); /* BFFF0011, -1073807343 */
    static final int VI_ERROR_INV_RSRC_NAME = (_VI_ERROR + 0x3FFF0012); /* BFFF0012, -1073807342 */
    static final int VI_ERROR_INV_ACC_MODE = (_VI_ERROR + 0x3FFF0013); /* BFFF0013, -1073807341 */
    static final int VI_ERROR_TMO = (_VI_ERROR + 0x3FFF0015); /* BFFF0015, -1073807339 */
    static final int VI_ERROR_CLOSING_FAILED = (_VI_ERROR + 0x3FFF0016); /* BFFF0016, -1073807338 */
    static final int VI_ERROR_INV_DEGREE = (_VI_ERROR + 0x3FFF001B); /* BFFF001B, -1073807333 */
    static final int VI_ERROR_INV_JOB_ID = (_VI_ERROR + 0x3FFF001C); /* BFFF001C, -1073807332 */
    static final int VI_ERROR_NSUP_ATTR = (_VI_ERROR + 0x3FFF001D); /* BFFF001D, -1073807331 */
    static final int VI_ERROR_NSUP_ATTR_STATE = (_VI_ERROR + 0x3FFF001E); /* BFFF001E, -1073807330 */
    static final int VI_ERROR_ATTR_READONLY = (_VI_ERROR + 0x3FFF001F); /* BFFF001F, -1073807329 */
    static final int VI_ERROR_INV_LOCK_TYPE = (_VI_ERROR + 0x3FFF0020); /* BFFF0020, -1073807328 */
    static final int VI_ERROR_INV_ACCESS_KEY = (_VI_ERROR + 0x3FFF0021); /* BFFF0021, -1073807327 */
    static final int VI_ERROR_INV_EVENT = (_VI_ERROR + 0x3FFF0026); /* BFFF0026, -1073807322 */
    static final int VI_ERROR_INV_MECH = (_VI_ERROR + 0x3FFF0027); /* BFFF0027, -1073807321 */
    static final int VI_ERROR_HNDLR_NINSTALLED = (_VI_ERROR + 0x3FFF0028); /* BFFF0028, -1073807320 */
    static final int VI_ERROR_INV_HNDLR_REF = (_VI_ERROR + 0x3FFF0029); /* BFFF0029, -1073807319 */
    static final int VI_ERROR_INV_CONTEXT = (_VI_ERROR + 0x3FFF002A); /* BFFF002A, -1073807318 */
    static final int VI_ERROR_QUEUE_OVERFLOW = (_VI_ERROR + 0x3FFF002D); /* BFFF002D, -1073807315 */
    static final int VI_ERROR_NENABLED = (_VI_ERROR + 0x3FFF002F); /* BFFF002F, -1073807313 */
    static final int VI_ERROR_ABORT = (_VI_ERROR + 0x3FFF0030); /* BFFF0030, -1073807312 */
    static final int VI_ERROR_RAW_WR_PROT_VIOL = (_VI_ERROR + 0x3FFF0034); /* BFFF0034, -1073807308 */
    static final int VI_ERROR_RAW_RD_PROT_VIOL = (_VI_ERROR + 0x3FFF0035); /* BFFF0035, -1073807307 */
    static final int VI_ERROR_OUTP_PROT_VIOL = (_VI_ERROR + 0x3FFF0036); /* BFFF0036, -1073807306 */
    static final int VI_ERROR_INP_PROT_VIOL = (_VI_ERROR + 0x3FFF0037); /* BFFF0037, -1073807305 */
    static final int VI_ERROR_BERR = (_VI_ERROR + 0x3FFF0038); /* BFFF0038, -1073807304 */
    static final int VI_ERROR_IN_PROGRESS = (_VI_ERROR + 0x3FFF0039); /* BFFF0039, -1073807303 */
    static final int VI_ERROR_INV_SETUP = (_VI_ERROR + 0x3FFF003A); /* BFFF003A, -1073807302 */
    static final int VI_ERROR_QUEUE_ERROR = (_VI_ERROR + 0x3FFF003B); /* BFFF003B, -1073807301 */
    static final int VI_ERROR_ALLOC = (_VI_ERROR + 0x3FFF003C); /* BFFF003C, -1073807300 */
    static final int VI_ERROR_INV_MASK = (_VI_ERROR + 0x3FFF003D); /* BFFF003D, -1073807299 */
    static final int VI_ERROR_IO = (_VI_ERROR + 0x3FFF003E); /* BFFF003E, -1073807298 */
    static final int VI_ERROR_INV_FMT = (_VI_ERROR + 0x3FFF003F); /* BFFF003F, -1073807297 */
    static final int VI_ERROR_NSUP_FMT = (_VI_ERROR + 0x3FFF0041); /* BFFF0041, -1073807295 */
    static final int VI_ERROR_LINE_IN_USE = (_VI_ERROR + 0x3FFF0042); /* BFFF0042, -1073807294 */
    static final int VI_ERROR_NSUP_MODE = (_VI_ERROR + 0x3FFF0046); /* BFFF0046, -1073807290 */
    static final int VI_ERROR_SRQ_NOCCURRED = (_VI_ERROR + 0x3FFF004A); /* BFFF004A, -1073807286 */
    static final int VI_ERROR_INV_SPACE = (_VI_ERROR + 0x3FFF004E); /* BFFF004E, -1073807282 */
    static final int VI_ERROR_INV_OFFSET = (_VI_ERROR + 0x3FFF0051); /* BFFF0051, -1073807279 */
    static final int VI_ERROR_INV_WIDTH = (_VI_ERROR + 0x3FFF0052); /* BFFF0052, -1073807278 */
    static final int VI_ERROR_NSUP_OFFSET = (_VI_ERROR + 0x3FFF0054); /* BFFF0054, -1073807276 */
    static final int VI_ERROR_NSUP_VAR_WIDTH = (_VI_ERROR + 0x3FFF0055); /* BFFF0055, -1073807275 */
    static final int VI_ERROR_WINDOW_NMAPPED = (_VI_ERROR + 0x3FFF0057); /* BFFF0057, -1073807273 */
    static final int VI_ERROR_RESP_PENDING = (_VI_ERROR + 0x3FFF0059); /* BFFF0059, -1073807271 */
    static final int VI_ERROR_NLISTENERS = (_VI_ERROR + 0x3FFF005F); /* BFFF005F, -1073807265 */
    static final int VI_ERROR_NCIC = (_VI_ERROR + 0x3FFF0060); /* BFFF0060, -1073807264 */
    static final int VI_ERROR_NSYS_CNTLR = (_VI_ERROR + 0x3FFF0061); /* BFFF0061, -1073807263 */
    static final int VI_ERROR_NSUP_OPER = (_VI_ERROR + 0x3FFF0067); /* BFFF0067, -1073807257 */
    static final int VI_ERROR_INTR_PENDING = (_VI_ERROR + 0x3FFF0068); /* BFFF0068, -1073807256 */
    static final int VI_ERROR_ASRL_PARITY = (_VI_ERROR + 0x3FFF006A); /* BFFF006A, -1073807254 */
    static final int VI_ERROR_ASRL_FRAMING = (_VI_ERROR + 0x3FFF006B); /* BFFF006B, -1073807253 */
    static final int VI_ERROR_ASRL_OVERRUN = (_VI_ERROR + 0x3FFF006C); /* BFFF006C, -1073807252 */
    static final int VI_ERROR_TRIG_NMAPPED = (_VI_ERROR + 0x3FFF006E); /* BFFF006E, -1073807250 */
    static final int VI_ERROR_NSUP_ALIGN_OFFSET = (_VI_ERROR + 0x3FFF0070); /* BFFF0070, -1073807248 */
    static final int VI_ERROR_USER_BUF = (_VI_ERROR + 0x3FFF0071); /* BFFF0071, -1073807247 */
    static final int VI_ERROR_RSRC_BUSY = (_VI_ERROR + 0x3FFF0072); /* BFFF0072, -1073807246 */
    static final int VI_ERROR_NSUP_WIDTH = (_VI_ERROR + 0x3FFF0076); /* BFFF0076, -1073807242 */
    static final int VI_ERROR_INV_PARAMETER = (_VI_ERROR + 0x3FFF0078); /* BFFF0078, -1073807240 */
    static final int VI_ERROR_INV_PROT = (_VI_ERROR + 0x3FFF0079); /* BFFF0079, -1073807239 */
    static final int VI_ERROR_INV_SIZE = (_VI_ERROR + 0x3FFF007B); /* BFFF007B, -1073807237 */
    static final int VI_ERROR_WINDOW_MAPPED = (_VI_ERROR + 0x3FFF0080); /* BFFF0080, -1073807232 */
    static final int VI_ERROR_NIMPL_OPER = (_VI_ERROR + 0x3FFF0081); /* BFFF0081, -1073807231 */
    static final int VI_ERROR_INV_LENGTH = (_VI_ERROR + 0x3FFF0083); /* BFFF0083, -1073807229 */
    static final int VI_ERROR_INV_MODE = (_VI_ERROR + 0x3FFF0091); /* BFFF0091, -1073807215 */
    static final int VI_ERROR_SESN_NLOCKED = (_VI_ERROR + 0x3FFF009C); /* BFFF009C, -1073807204 */
    static final int VI_ERROR_MEM_NSHARED = (_VI_ERROR + 0x3FFF009D); /* BFFF009D, -1073807203 */
    static final int VI_ERROR_LIBRARY_NFOUND = (_VI_ERROR + 0x3FFF009E); /* BFFF009E, -1073807202 */
    static final int VI_ERROR_NSUP_INTR = (_VI_ERROR + 0x3FFF009F); /* BFFF009F, -1073807201 */
    static final int VI_ERROR_INV_LINE = (_VI_ERROR + 0x3FFF00A0); /* BFFF00A0, -1073807200 */
    static final int VI_ERROR_FILE_ACCESS = (_VI_ERROR + 0x3FFF00A1); /* BFFF00A1, -1073807199 */
    static final int VI_ERROR_FILE_IO = (_VI_ERROR + 0x3FFF00A2); /* BFFF00A2, -1073807198 */
    static final int VI_ERROR_NSUP_LINE = (_VI_ERROR + 0x3FFF00A3); /* BFFF00A3, -1073807197 */
    static final int VI_ERROR_NSUP_MECH = (_VI_ERROR + 0x3FFF00A4); /* BFFF00A4, -1073807196 */
    static final int VI_ERROR_INTF_NUM_NCONFIG = (_VI_ERROR + 0x3FFF00A5); /* BFFF00A5, -1073807195 */
    static final int VI_ERROR_CONN_LOST = (_VI_ERROR + 0x3FFF00A6); /* BFFF00A6, -1073807194 */
    static final int VI_ERROR_MACHINE_NAVAIL = (_VI_ERROR + 0x3FFF00A7); /* BFFF00A7, -1073807193 */
    static final int VI_ERROR_NPERMISSION = (_VI_ERROR + 0x3FFF00A8); /* BFFF00A8, -1073807192 */

    /*- Other VISA Definitions --------------------------------------------------*/
    //static final int VI_VERSION_MAJOR(ver)       =((((ViVersion)ver) & 0xFFF00000L) >> 20);
    //static final int VI_VERSION_MINOR(ver)       =((((ViVersion)ver) & 0x000FFF00L) >>  8);
    //static final int VI_VERSION_SUBMINOR(ver)    =((((ViVersion)ver) & 0x000000FFL)      );
    static final int VI_FIND_BUFLEN = (256);
    static final int VI_INTF_GPIB = (1);
    static final int VI_INTF_VXI = (2);
    static final int VI_INTF_GPIB_VXI = (3);
    static final int VI_INTF_ASRL = (4);
    static final int VI_INTF_TCPIP = (6);
    static final int VI_INTF_USB = (7);
    static final int VI_PROT_NORMAL = (1);
    static final int VI_PROT_FDC = (2);
    static final int VI_PROT_HS488 = (3);
    static final int VI_PROT_4882_STRS = (4);
    static final int VI_PROT_USBTMC_VENDOR = (5);
    static final int VI_FDC_NORMAL = (1);
    static final int VI_FDC_STREAM = (2);
    static final int VI_LOCAL_SPACE = (0);
    static final int VI_A16_SPACE = (1);
    static final int VI_A24_SPACE = (2);
    static final int VI_A32_SPACE = (3);
    static final int VI_A64_SPACE = (4);
    static final int VI_OPAQUE_SPACE = (0xFFFF);
    static final int VI_UNKNOWN_LA = (-1);
    static final int VI_UNKNOWN_SLOT = (-1);
    static final int VI_UNKNOWN_LEVEL = (-1);
    static final int VI_QUEUE = (1);
    static final int VI_HNDLR = (2);
    static final int VI_SUSPEND_HNDLR = (4);
    static final int VI_ALL_MECH = (0xFFFF);
    static final int VI_ANY_HNDLR = (0);
    static final int VI_TRIG_ALL = (-2);
    static final int VI_TRIG_SW = (-1);
    static final int VI_TRIG_TTL0 = (0);
    static final int VI_TRIG_TTL1 = (1);
    static final int VI_TRIG_TTL2 = (2);
    static final int VI_TRIG_TTL3 = (3);
    static final int VI_TRIG_TTL4 = (4);
    static final int VI_TRIG_TTL5 = (5);
    static final int VI_TRIG_TTL6 = (6);
    static final int VI_TRIG_TTL7 = (7);
    static final int VI_TRIG_ECL0 = (8);
    static final int VI_TRIG_ECL1 = (9);
    static final int VI_TRIG_PANEL_IN = (27);
    static final int VI_TRIG_PANEL_OUT = (28);
    static final int VI_TRIG_PROT_DEFAULT = (0);
    static final int VI_TRIG_PROT_ON = (1);
    static final int VI_TRIG_PROT_OFF = (2);
    static final int VI_TRIG_PROT_SYNC = (5);
    static final int VI_READ_BUF = (1);
    static final int VI_WRITE_BUF = (2);
    static final int VI_READ_BUF_DISCARD = (4);
    static final int VI_WRITE_BUF_DISCARD = (8);
    static final int VI_IO_IN_BUF = (16);
    static final int VI_IO_OUT_BUF = (32);
    static final int VI_IO_IN_BUF_DISCARD = (64);
    static final int VI_IO_OUT_BUF_DISCARD = (128);
    static final int VI_FLUSH_ON_ACCESS = (1);
    static final int VI_FLUSH_WHEN_FULL = (2);
    static final int VI_FLUSH_DISABLE = (3);
    static final int VI_NMAPPED = (1);
    static final int VI_USE_OPERS = (2);
    static final int VI_DEREF_ADDR = (3);
    static final int VI_TMO_IMMEDIATE = (0);
    static final int VI_TMO_INFINITE = (0xFFFFFFFF);
    static final int VI_NO_LOCK = (0);
    static final int VI_EXCLUSIVE_LOCK = (1);
    static final int VI_SHARED_LOCK = (2);
    static final int VI_LOAD_CONFIG = (4);
    static final int VI_NO_SEC_ADDR = (0xFFFF);
    static final int VI_ASRL_PAR_NONE = (0);
    static final int VI_ASRL_PAR_ODD = (1);
    static final int VI_ASRL_PAR_EVEN = (2);
    static final int VI_ASRL_PAR_MARK = (3);
    static final int VI_ASRL_PAR_SPACE = (4);
    static final int VI_ASRL_STOP_ONE = (10);
    static final int VI_ASRL_STOP_ONE5 = (15);
    static final int VI_ASRL_STOP_TWO = (20);
    static final int VI_ASRL_FLOW_NONE = (0);
    static final int VI_ASRL_FLOW_XON_XOFF = (1);
    static final int VI_ASRL_FLOW_RTS_CTS = (2);
    static final int VI_ASRL_FLOW_DTR_DSR = (4);
    static final int VI_ASRL_END_NONE = (0);
    static final int VI_ASRL_END_LAST_BIT = (1);
    static final int VI_ASRL_END_TERMCHAR = (2);
    static final int VI_ASRL_END_BREAK = (3);
    static final int VI_STATE_ASSERTED = (1);
    static final int VI_STATE_UNASSERTED = (0);
    static final int VI_STATE_UNKNOWN = (-1);
    static final int VI_BIG_ENDIAN = (0);
    static final int VI_LITTLE_ENDIAN = (1);
    static final int VI_DATA_PRIV = (0);
    static final int VI_DATA_NPRIV = (1);
    static final int VI_PROG_PRIV = (2);
    static final int VI_PROG_NPRIV = (3);
    static final int VI_BLCK_PRIV = (4);
    static final int VI_BLCK_NPRIV = (5);
    static final int VI_D64_PRIV = (6);
    static final int VI_D64_NPRIV = (7);
    static final int VI_WIDTH_8 = (1);
    static final int VI_WIDTH_16 = (2);
    static final int VI_WIDTH_32 = (4);
    static final int VI_GPIB_REN_DEASSERT = (0);
    static final int VI_GPIB_REN_ASSERT = (1);
    static final int VI_GPIB_REN_DEASSERT_GTL = (2);
    static final int VI_GPIB_REN_ASSERT_ADDRESS = (3);
    static final int VI_GPIB_REN_ASSERT_LLO = (4);
    static final int VI_GPIB_REN_ASSERT_ADDRESS_LLO = (5);
    static final int VI_GPIB_REN_ADDRESS_GTL = (6);
    static final int VI_GPIB_ATN_DEASSERT = (0);
    static final int VI_GPIB_ATN_ASSERT = (1);
    static final int VI_GPIB_ATN_DEASSERT_HANDSHAKE = (2);
    static final int VI_GPIB_ATN_ASSERT_IMMEDIATE = (3);
    static final int VI_GPIB_HS488_DISABLED = (0);
    static final int VI_GPIB_HS488_NIMPL = (-1);
    static final int VI_GPIB_UNADDRESSED = (0);
    static final int VI_GPIB_TALKER = (1);
    static final int VI_GPIB_LISTENER = (2);
    static final int VI_VXI_CMD16 = (0x0200);
    static final int VI_VXI_CMD16_RESP16 = (0x0202);
    static final int VI_VXI_RESP16 = (0x0002);
    static final int VI_VXI_CMD32 = (0x0400);
    static final int VI_VXI_CMD32_RESP16 = (0x0402);
    static final int VI_VXI_CMD32_RESP32 = (0x0404);
    static final int VI_VXI_RESP32 = (0x0004);
    static final int VI_ASSERT_SIGNAL = (-1);
    static final int VI_ASSERT_USE_ASSIGNED = (0);
    static final int VI_ASSERT_IRQ1 = (1);
    static final int VI_ASSERT_IRQ2 = (2);
    static final int VI_ASSERT_IRQ3 = (3);
    static final int VI_ASSERT_IRQ4 = (4);
    static final int VI_ASSERT_IRQ5 = (5);
    static final int VI_ASSERT_IRQ6 = (6);
    static final int VI_ASSERT_IRQ7 = (7);
    static final int VI_UTIL_ASSERT_SYSRESET = (1);
    static final int VI_UTIL_ASSERT_SYSFAIL = (2);
    static final int VI_UTIL_DEASSERT_SYSFAIL = (3);
    static final int VI_VXI_CLASS_MEMORY = (0);
    static final int VI_VXI_CLASS_EXTENDED = (1);
    static final int VI_VXI_CLASS_MESSAGE = (2);
    static final int VI_VXI_CLASS_REGISTER = (3);
    static final int VI_VXI_CLASS_OTHER = (4);
    /*- National Instruments ----------------------------------------------------*/
    static final int VI_INTF_RIO = (8);
    static final int VI_INTF_FIREWIRE = (9);
    static final int VI_ATTR_SYNC_MXI_ALLOW_EN = (0x3FFF0161); /* ViBoolean, read/write */

    /* This is for VXI SERVANT resources */
    static final int VI_EVENT_VXI_DEV_CMD = (0xBFFF200F);
    static final int VI_ATTR_VXI_DEV_CMD_TYPE = (0x3FFF4037); /* ViInt16, read-only */

    static final int VI_ATTR_VXI_DEV_CMD_VALUE = (0x3FFF4038); /* ViUInt32, read-only */

    static final int VI_VXI_DEV_CMD_TYPE_16 = (16);
    static final int VI_VXI_DEV_CMD_TYPE_32 = (32);
    //ViStatus _VI_FUNC viVxiServantResponse(ViSession vi, ViInt16 mode, ViUInt32 resp);
    /* mode values include VI_VXI_RESP16, VI_VXI_RESP32, and the next 2 values */
    static final int VI_VXI_RESP_NONE = (0);
    static final int VI_VXI_RESP_PROT_ERROR = (-1);
    /* This allows extended Serial support on Win32 and on NI ENET Serial products */
    static final int VI_ATTR_ASRL_DISCARD_NULL = (0x3FFF00B0);
    static final int VI_ATTR_ASRL_CONNECTED = (0x3FFF01BB);
    static final int VI_ATTR_ASRL_BREAK_STATE = (0x3FFF01BC);
    static final int VI_ATTR_ASRL_BREAK_LEN = (0x3FFF01BD);
    static final int VI_ATTR_ASRL_ALLOW_TRANSMIT = (0x3FFF01BE);
    static final int VI_ATTR_ASRL_WIRE_MODE = (0x3FFF01BF);
    static final int VI_ASRL_WIRE_485_4 = (0);
    static final int VI_ASRL_WIRE_485_2_DTR_ECHO = (1);
    static final int VI_ASRL_WIRE_485_2_DTR_CTRL = (2);
    static final int VI_ASRL_WIRE_485_2_AUTO = (3);
    static final int VI_ASRL_WIRE_232_DTE = (128);
    static final int VI_ASRL_WIRE_232_DCE = (129);
    static final int VI_ASRL_WIRE_232_AUTO = (130);
    static final int VI_EVENT_ASRL_BREAK = (0x3FFF2023);
    static final int VI_EVENT_ASRL_CTS = (0x3FFF2029);
    static final int VI_EVENT_ASRL_DSR = (0x3FFF202A);
    static final int VI_EVENT_ASRL_DCD = (0x3FFF202C);
    static final int VI_EVENT_ASRL_RI = (0x3FFF202E);
    static final int VI_EVENT_ASRL_CHAR = (0x3FFF2035);
    static final int VI_EVENT_ASRL_TERMCHAR = (0x3FFF2024);
    /* This macro is supported for all Win32 compilers, including CVI. */
    /* This macro is not supported on other platforms. */
    static final int VI_ATTR_PXI_DEV_NUM = (0x3FFF0201);
    static final int VI_ATTR_PXI_FUNC_NUM = (0x3FFF0202);
    /* The following 2 attributes were incorrectly implemented in earlier
    versions of NI-VISA.  You should now query VI_ATTR_MANF_ID or
    VI_ATTR_MODEL_CODE.  Those attributes contain sub-vendor information
    when it exists.  To get both the actual primary and subvendor codes
    from the device, you should call viIn16 using VI_PXI_CFG_SPACE. */
    static final int VI_ATTR_PXI_SUB_MANF_ID = (0x3FFF0203);
    static final int VI_ATTR_PXI_SUB_MODEL_CODE = (0x3FFF0204);
    static final int VI_ATTR_PXI_BUS_NUM = (0x3FFF0205);
    static final int VI_ATTR_PXI_CHASSIS = (0x3FFF0206);
    static final int VI_ATTR_PXI_SLOTPATH = (0xBFFF0207);
    static final int VI_ATTR_PXI_SLOT_LBUS_LEFT = (0x3FFF0208);
    static final int VI_ATTR_PXI_SLOT_LBUS_RIGHT = (0x3FFF0209);
    static final int VI_ATTR_PXI_TRIG_BUS = (0x3FFF020A);
    static final int VI_ATTR_PXI_STAR_TRIG_BUS = (0x3FFF020B);
    static final int VI_ATTR_PXI_STAR_TRIG_LINE = (0x3FFF020C);
    static final int VI_ATTR_PXI_SRC_TRIG_BUS = (0x3FFF020D);
    static final int VI_ATTR_PXI_DEST_TRIG_BUS = (0x3FFF020E);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR0 = (0x3FFF0211);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR1 = (0x3FFF0212);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR2 = (0x3FFF0213);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR3 = (0x3FFF0214);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR4 = (0x3FFF0215);
    static final int VI_ATTR_PXI_MEM_TYPE_BAR5 = (0x3FFF0216);
    static final int VI_ATTR_PXI_MEM_BASE_BAR0 = (0x3FFF0221);
    static final int VI_ATTR_PXI_MEM_BASE_BAR1 = (0x3FFF0222);
    static final int VI_ATTR_PXI_MEM_BASE_BAR2 = (0x3FFF0223);
    static final int VI_ATTR_PXI_MEM_BASE_BAR3 = (0x3FFF0224);
    static final int VI_ATTR_PXI_MEM_BASE_BAR4 = (0x3FFF0225);
    static final int VI_ATTR_PXI_MEM_BASE_BAR5 = (0x3FFF0226);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR0 = (0x3FFF0231);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR1 = (0x3FFF0232);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR2 = (0x3FFF0233);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR3 = (0x3FFF0234);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR4 = (0x3FFF0235);
    static final int VI_ATTR_PXI_MEM_SIZE_BAR5 = (0x3FFF0236);
    static final int VI_ATTR_PXI_RECV_INTR_SEQ = (0x3FFF4240);
    static final int VI_ATTR_PXI_RECV_INTR_DATA = (0x3FFF4241);
    static final int VI_EVENT_PXI_INTR = (0x3FFF2022);
    static final int VI_INTF_PXI = (5);
    static final int VI_PXI_ALLOC_SPACE = (9);
    static final int VI_PXI_CFG_SPACE = (10);
    static final int VI_PXI_BAR0_SPACE = (11);
    static final int VI_PXI_BAR1_SPACE = (12);
    static final int VI_PXI_BAR2_SPACE = (13);
    static final int VI_PXI_BAR3_SPACE = (14);
    static final int VI_PXI_BAR4_SPACE = (15);
    static final int VI_PXI_BAR5_SPACE = (16);
    static final int VI_PXI_ADDR_NONE = (0);
    static final int VI_PXI_ADDR_MEM = (1);
    static final int VI_PXI_ADDR_IO = (2);
    static final int VI_PXI_ADDR_CFG = (3);
    static final int VI_TRIG_PROT_RESERVE = (6);
    static final int VI_TRIG_PROT_UNRESERVE = (7);
    static final int VI_UNKNOWN_CHASSIS = (-1);
    static final int VI_ATTR_USB_BULK_OUT_PIPE = 0x3FFF01A2;
    static final int VI_ATTR_USB_BULK_IN_PIPE = 0x3FFF01A3;
    static final int VI_ATTR_USB_INTR_IN_PIPE = 0x3FFF01A4;
    static final int VI_ATTR_USB_CLASS = 0x3FFF01A5;
    static final int VI_ATTR_USB_SUBCLASS = 0x3FFF01A6;
    static final int VI_ATTR_USB_ALT_SETTING = 0x3FFF01A8;
    static final int VI_ATTR_USB_END_IN = 0x3FFF01A9;
    static final int VI_ATTR_USB_NUM_INTFCS = 0x3FFF01AA;
    static final int VI_ATTR_USB_NUM_PIPES = 0x3FFF01AB;
    static final int VI_ATTR_USB_BULK_OUT_STATUS = 0x3FFF01AC;
    static final int VI_ATTR_USB_BULK_IN_STATUS = 0x3FFF01AD;
    static final int VI_ATTR_USB_INTR_IN_STATUS = 0x3FFF01AE;
    static final int VI_ATTR_USB_CTRL_PIPE = 0x3FFF01B0;
    static final int VI_USB_PIPE_STATE_UNKNOWN = (-1);
    static final int VI_USB_PIPE_READY = (0);
    static final int VI_USB_PIPE_STALLED = (1);
    static final int VI_USB_END_NONE = (0);
    static final int VI_USB_END_SHORT = (4);
    static final int VI_USB_END_SHORT_OR_COUNT = (5);
    static final int VI_ATTR_FIREWIRE_DEST_UPPER_OFFSET = (0x3FFF01F0);
    static final int VI_ATTR_FIREWIRE_SRC_UPPER_OFFSET = (0x3FFF01F1);
    static final int VI_ATTR_FIREWIRE_WIN_UPPER_OFFSET = (0x3FFF01F2);
    static final int VI_ATTR_FIREWIRE_VENDOR_ID = (0x3FFF01F3);
    static final int VI_ATTR_FIREWIRE_LOWER_CHIP_ID = (0x3FFF01F4);
    static final int VI_ATTR_FIREWIRE_UPPER_CHIP_ID = (0x3FFF01F5);
    static final int VI_FIREWIRE_DFLT_SPACE = (5);

    static def libc = NativeLibrary.getInstance(Platform.isWindows() ? "visa32" : "visa32")
    final int sesn

    void setSesn(int newSesn){
        this.sesn = newSesn
        println "Visa opened: $sesn"
    }

    void close(){
        viClose(sesn)
        println "Visa closed: $sesn"
    }

    Visa(){
        IntByReference defaultRM = new IntByReference()
        if (0 == viOpenDefaultRM(defaultRM)) {
            setSesn(defaultRM.getValue())
        } else {
            throw new IllegalStateException('Cannot open default RM')
        }
    }

    String toStatusString(int status) {
        byte[] statusString = new byte[256]
        viStatusDesc(sesn, status, statusString)
        Integer.toString(status) + ": " + Native.toString(statusString)
    }

    def open(String rsrcName){
        open(rsrcName, 0, 0)
    }

    def open(String rsrcName, int accessMode, int openTimeout){
        IntByReference vi = new IntByReference()
        if (0 != viOpen(sesn, rsrcName, accessMode, openTimeout, vi)){
            throw new IllegalStateException("Cannot open: $rsrcName")
        }
        String name = getRsrcClass(vi.getValue())
        if (name.equals('INSTR')){
            return new VisaInst(this, vi.getValue())
        } else if (name.equals('RAW')) {
            return new VisaRaw(this, vi.getValue())
        } else {
            println "Not supported class: $name"
            viClose(vi.getValue())
            throw new IllegalArgumentException("Not supported class: $name")
        }
    }

    int getAttributeInt(int attr){
        getAttributeInt(sesn, attr)
    }

    int getAttributeInt(int inst, int attr){
        IntByReference retval = new IntByReference()
        def status = viGetAttribute(inst, (int)attr, retval)
        if (0 != status){
            throw new IllegalStateException("Error reading integer attribute of instrument: $status")
        }
        retval.getValue()
    }

    String getAttributeString(int attr){
        getAttributeString(sesn, attr)
    }

    String getAttributeString(int inst, int attr){
        byte[] retval = new byte[256]
        def status = viGetAttribute(inst, (int)attr, retval)
        if (0 != status){
            throw new IllegalStateException("Error reading string attribute of instrument: $status")
        }
        Native.toString(retval)
    }

    String getRsrcClass(int inst){
        getAttributeString(inst,(int)0xBFFF0001)
    }

    def methodMissing(String name, args) {
        //println "Visa methodMissing: $name, with args: $args"
        def method = libc.getFunction(name)
        if (method==null) {
            throw new MissingMethodException(name, getClass(), args)
        }
        method.invokeInt(args)
    }
}
