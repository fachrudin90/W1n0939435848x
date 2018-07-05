package com.tamboraagungmakmur.winwin.Utils;

/**
 * Created by Tambora on 24/02/2017.
 */
public class AppConf {

    public static final String APIKEY = "dGFtYm9yYTp0YW1ib3I0MTIzLg==";
    public static String BASE_URL1 = "http://118.98.64.44/";
    public static String BASE_URL = "http://118.98.64.44/winwin/api/";
//    public static String BASE_URL = "https://hq.ppgwinwin.com:445/";



//    public static String BASE_URL_IMG = "https://hq.ppgwinwin.com/winwin";
    public static String BASE_URL_IMG = "http://118.98.64.44/winwin";
    public static String BASE_URL_API_BCA = "https://hq.ppgwinwin.com/winwin/home/";

    public static String URL_PARENT = "";
    public static String URL_SERV_ROOT = BASE_URL + URL_PARENT;
    public static String URL_SERV_IMG = URL_SERV_ROOT + "files/";
    public static String httpTag = "HTTPTAG";


    public static String URL_SERV = BASE_URL + URL_PARENT;
    public static String URL_LOGIN = URL_SERV + "session/login";
    public static String URL_TOKEN = URL_SERV + "session/token";
    public static String URL_LOGOUT = URL_SERV + "session/logout";
    public static String URL_USER_GETCHAT = URL_SERV + "user/getchat";
    public static String URL_USER_SENDCHAT = URL_SERV + "user/sendchat";
    public static String URL_KLIEN_ALL = URL_SERV + "klien/all";
    public static String URL_KLIEN_RESUME = URL_SERV + "klien/resume";
    public static String URL_EMAIL_TEMPLATE = URL_SERV + "refEmailTemplate/all";
    public static String URL_REF_KOTA = URL_SERV + "refKota/all";
    public static String URL_REF_BANK_KLIEN = URL_SERV + "refBankClient/all";
    public static String URL_REF_PERUSAHAAN = URL_SERV + "refPerusahaan/all";
    public static String URL_REF_EMAIL_SYSTEM = URL_SERV + "refEmailSystem/all";
    public static String URL_REF_RLC= URL_SERV + "refRlc/all";
    public static String URL_REF_AREA= URL_SERV + "refArea/all";
    public static String URL_REF_FREKUENSI= URL_SERV + "refFreq/all";
    public static String URL_REF_TABEL= URL_SERV + "refTable/all";
    public static String URL_REF_TAHAP= URL_SERV + "refTahap/all";
    public static String URL_REF_KODEPOS= URL_SERV + "refKodePos/all";
    public static String URL_REF_JABATAN= URL_SERV + "refJabatan/all";
    public static String URL_REF_TRXTIPE= URL_SERV + "refTrxType/all";
    public static String URL_REF_WORK_ITEM= URL_SERV + "refWorkItem/all";
    public static String URL_REF_STATUS_RUMAH= URL_SERV + "refStatusRumah/all";
    public static String URL_REF_STATUS_ARTIKEL= URL_SERV + "refNewsStatus/all";
    public static String URL_REF_JENIS_BAYAR= URL_SERV + "refJenisBayar/all";
    public static String URL_REF_MEDIA_BAYAR= URL_SERV + "refMediaBayar/all";
    public static String URL_REF_HUB_KELUARGA= URL_SERV + "refHubKlrg/all";
    public static String URL_REF_PROSES_IMPORT= URL_SERV + "refProsesImport/all";
    public static String URL_REF_JK= URL_SERV + "refJK/all";
    public static String URL_REF_STATUS_KLIEN= URL_SERV + "refClientStatus/all";
    public static String URL_REF_TAHAP_PENGAJUAN= URL_SERV + "refPengajuanTahap/all";
    public static String URL_REF_TAHAP_PENCAIRAN= URL_SERV + "refPencairanTahap/all";
    public static String URL_REF_STATUS_PENGAJUAN= URL_SERV + "refPengajuanStatus/all";
    public static String URL_REF_STATUS_TESTIMONI= URL_SERV + "refTestimoniStatus/all";
    public static String URL_REF_JENIS_PEMBAYARAN= URL_SERV + "refJenisPembayaran/all";
    public static String URL_REF_MEDIA_PEMBAYARAN= URL_SERV + "refMediaPembayaran/all";
    public static String URL_REF_SYARAT_DAFTAR= URL_SERV + "refSyaratDaftar/all";
    public static String URL_REF_SYARAT_PENGAJUAN= URL_SERV + "refSyaratPengajuan/all";

    public static String URL_REF_PERUSAHAAN_STORE = URL_SERV + "refPerusahaan/store";
    public static String URL_REF_PERUSAHAAN_UPDATE = URL_SERV + "refPerusahaan/update";
    public static String URL_REF_PERUSAHAAN_DETAIL = URL_SERV + "refPerusahaan/detail";

    public static String URL_REF_RLC_STORE = URL_SERV + "refRlc/store";
    public static String URL_REF_RLC_UPDATE = URL_SERV + "refRlc/update";
    public static String URL_REF_RLC_DETAIL = URL_SERV + "refRlc/detail";

    public static String URL_REF_AREA_STORE = URL_SERV + "refArea/store";
    public static String URL_REF_AREA_UPDATE = URL_SERV + "refArea/update";
    public static String URL_REF_AREA_DETAIL = URL_SERV + "refArea/detail";


    public static String URL_REF_KODEPOS_STORE = URL_SERV + "refKodePos/store";
    public static String URL_REF_KODEPOS_UPDATE = URL_SERV + "refKodePos/update";
    public static String URL_REF_KODEPOS_DETAIL = URL_SERV + "refKodePos/detail";

    public static String URL_REF_JABATAN_STORE = URL_SERV + "refJabatan/store";
    public static String URL_REF_JABATAN_UPDATE = URL_SERV + "refJabatan/update";
    public static String URL_REF_JABATAN_DETAIL = URL_SERV + "refJabatan/detail";

    public static String URL_REF_TIPE_TRANSAKSI_STORE = URL_SERV + "refTrxType/store";
    public static String URL_REF_TIPE_TRANSAKSI_UPDATE = URL_SERV + "refTrxType/update";
    public static String URL_REF_TIPE_TRANSAKSI_DETAIL = URL_SERV + "refTrxType/detail";

    public static String URL_REF_WORK_ITEM_STORE = URL_SERV + "refWorkItem/store";
    public static String URL_REF_WORK_ITEM_UPDATE = URL_SERV + "refWorkItem/update";
    public static String URL_REF_WORK_ITEM_DETAIL = URL_SERV + "refWorkItem/detail";

    public static String URL_REF_STATUS_RUMAH_STORE = URL_SERV + "refStatusRumah/store";
    public static String URL_REF_STATUS_RUMAH_UPDATE = URL_SERV + "refStatusRumah/update";
    public static String URL_REF_STATUS_RUMAH_DETAIL = URL_SERV + "refStatusRumah/detail";

    public static String URL_REF_STATUS_ARTIKEL_STORE = URL_SERV + "refNewsStatus/store";
    public static String URL_REF_STATUS_ARTIKEL_UPDATE = URL_SERV + "refNewsStatus/update";
    public static String URL_REF_STATUS_ARTIKEL_DETAIL = URL_SERV + "refNewsStatus/detail";

    public static String URL_REF_JENIS_BAYAR_STORE = URL_SERV + "refJenisBayar/store";
    public static String URL_REF_JENIS_BAYAR_UPDATE = URL_SERV + "refJenisBayar/update";
    public static String URL_REF_JENIS_BAYAR_DETAIL = URL_SERV + "refJenisBayar/detail";

    public static String URL_REF_MEDIA_BAYAR_STORE = URL_SERV + "refMediaBayar/store";
    public static String URL_REF_MEDIA_BAYAR_UPDATE = URL_SERV + "refMediaBayar/update";
    public static String URL_REF_MEDIA_BAYAR_DETAIL = URL_SERV + "refMediaBayar/detail";

    public static String URL_REF_HUB_KLGR_STORE = URL_SERV + "refHubKlrg/store";
    public static String URL_REF_HUB_KLGR_UPDATE = URL_SERV + "refHubKlrg/update";
    public static String URL_REF_HUB_KLGR_DETAIL = URL_SERV + "refHubKlrg/detail";

    public static String URL_REF_STATUS_KLIEN_STORE = URL_SERV + "refClientStatus/store";
    public static String URL_REF_STATUS_KLIEN_UPDATE = URL_SERV + "refClientStatus/update";
    public static String URL_REF_STATUS_KLIEN_DETAIL = URL_SERV + "refClientStatus/detail";

    public static String URL_REF_TAHAP_PENGAJUAN_STORE = URL_SERV + "refPengajuanTahap/store";
    public static String URL_REF_TAHAP_PENGAJUAN_UPDATE = URL_SERV + "refPengajuanTahap/update";
    public static String URL_REF_TAHAP_PENGAJUAN_DETAIL = URL_SERV + "refPengajuanTahap/detail";

    public static String URL_REF_TAHAP_PENCAIRAN_STORE = URL_SERV + "refPencairanTahap/store";
    public static String URL_REF_TAHAP_PENCAIRAN_UPDATE = URL_SERV + "refPencairanTahap/update";
    public static String URL_REF_TAHAP_PENCAIRAN_DETAIL = URL_SERV + "refPencairanTahap/detail";


    public static String URL_REF_STATUS_PENGAJUAN_STORE = URL_SERV + "refPengajuanStatus/store";
    public static String URL_REF_STATUS_PENGAJUAN_UPDATE = URL_SERV + "refPengajuanStatus/update";
    public static String URL_REF_STATUS_PENGAJUAN_DETAIL = URL_SERV + "refPengajuanStatus/detail";


    public static String URL_REF_STATUS_TESTIMONI_STORE = URL_SERV + "refTestimoniStatus/store";
    public static String URL_REF_STATUS_TESTIMONI_UPDATE = URL_SERV + "refTestimoniStatus/update";
    public static String URL_REF_STATUS_TESTIMONI_DETAIL = URL_SERV + "refTestimoniStatus/detail";

    public static String URL_REF_JENIS_PEMBAYARAN_STORE = URL_SERV + "refJenisPembayaran/store";
    public static String URL_REF_JENIS_PEMBAYARAN_UPDATE = URL_SERV + "refJenisPembayaran/update";
    public static String URL_REF_JENIS_PEMBAYARAN_DETAIL = URL_SERV + "refJenisPembayaran/detail";

    public static String URL_REF_MEDIA_PEMBAYARAN_STORE = URL_SERV + "refMediaPembayaran/store";
    public static String URL_REF_MEDIA_PEMBAYARAN_UPDATE = URL_SERV + "refMediaPembayaran/update";
    public static String URL_REF_MEDIA_PEMBAYARAN_DETAIL = URL_SERV + "refMediaPembayaran/detail";

    public static String URL_REF_SYARAT_DAFTAR_STORE = URL_SERV + "refSyaratDaftar/store";
    public static String URL_REF_SYARAT_DAFTAR_UPDATE = URL_SERV + "refSyaratDaftar/update";
    public static String URL_REF_SYARAT_DAFTAR_DETAIL = URL_SERV + "refSyaratDaftar/detail";

    public static String URL_REF_SYARAT_PENGAJUAN_STORE = URL_SERV + "refSyaratPengajuan/store";
    public static String URL_REF_SYARAT_PENGAJUAN_UPDATE = URL_SERV + "refSyaratPengajuan/update";
    public static String URL_REF_SYARAT_PENGAJUAN_DETAIL = URL_SERV + "refSyaratPengajuan/detail";

    public static String URL_REF_FREKUENSI_STORE = URL_SERV + "refFreq/store";
    public static String URL_REF_FREKUENSI_UPDATE = URL_SERV + "refFreq/update";
    public static String URL_REF_FREKUENSI_DETAIL = URL_SERV + "refFreq/detail";

    public static String URL_REF_TABEL_STORE = URL_SERV + "refTable/store";
    public static String URL_REF_TABEL_UPDATE = URL_SERV + "refTable/update";
    public static String URL_REF_TABEL_DETAIL = URL_SERV + "refTable/detail";

    public static String URL_REF_TAHAP_STORE = URL_SERV + "refTahap/store";
    public static String URL_REF_TAHAP_UPDATE = URL_SERV + "refTahap/update";
    public static String URL_REF_TAHAP_DETAIL = URL_SERV + "refTahap/detail";

    public static String URL_REF_PROSES_IMPORT_STORE = URL_SERV + "refProsesImport/store";
    public static String URL_REF_PROSES_IMPORT_UPDATE = URL_SERV + "refProsesImport/update";
    public static String URL_REF_PROSES_IMPORT_DETAIL = URL_SERV + "refProsesImport/detail";


    public static String URL_REF_PEKERJAAN = URL_SERV + "refPekerjaan/all";
    public static String URL_REF_PEKERJAAN_DETAIL = URL_SERV + "refPekerjaan/detail";
    public static String URL_REF_PEKERJAAN_UPDATE = URL_SERV + "refPekerjaan/update";
    public static String URL_REF_PEKERJAAN_STORE = URL_SERV + "refPekerjaan/store";
    public static String URL_REF_BANK_KLIEN_DETAIL = URL_SERV + "refBankClient/detail";
    public static String URL_REF_BANK_KLIEN_UPDATE = URL_SERV + "refBankClient/update";
    public static String URL_REF_BANK_KLIEN_STORE = URL_SERV + "refBankClient/store";
    public static String URL_REF_KOTA_DETAIL = URL_SERV + "refKodePos/detail";
    public static String URL_REF_KOTA_UPDATE = URL_SERV + "refKodePos/update";
    public static String URL_EMAIL_TEMPLATE_STORE = URL_SERV + "refEmailTemplate/store";
    public static String URL_EMAIL_TEMPLATE_DETAIL = URL_SERV + "refEmailTemplate/detail";
    public static String URL_EMAIL_TEMPLATE_UPDATE = URL_SERV + "refEmailTemplate/update";
    public static String URL_KLIEN_DETAIL = URL_SERV + "klien/detail";
    public static String URL_KLIEN_FILES = URL_SERV + "klien/filesbyclient";
    public static String URL_KLIEN_FILES_DELETE = URL_SERV + "klien/file";
    public static String URL_KLIEN_UPLOAD_FILE = URL_SERV + "klien/uploadfile";
    public static String URL_KLIEN_LINKS = URL_SERV + "klien/links";
    public static String URL_KLIEN_NOTE = URL_SERV + "klien/note";
    public static String URL_KLIEN_NOTES = URL_SERV + "klien/notesbyclient";
    public static String URL_REFERRAL_AVAILABLE = URL_SERV + "referral/available";
    public static String URL_CHECKLIST_ALL = URL_SERV + "refChecklist/all";
    public static String URL_KLIEN_CHECKLIST = URL_SERV + "klien/checklist";
    public static String URL_KLIEN_CHECKLIST2 = URL_SERV + "klien/checklist2";
    public static String URL_ARTICLE_ALL = URL_SERV + "article/all";
    public static String URL_USER_KARYAWAN_ALL = URL_SERV + "user/allkaryawan";
    public static String URL_USER_KARYAWAN = URL_SERV + "user/karyawan";
    public static String URL_USER_USER = URL_SERV + "user/user";
    public static String URL_USER_DATA = URL_SERV + "user/userdata";
    public static String URL_USER_KARDATA = URL_SERV + "user/karyawandata";
    public static String URL_USER_STORE = URL_SERV + "user/store";
    public static String URL_USER_STORE1 = URL_SERV + "user/store1";
    public static String URL_TESTIMONI_ALL = URL_SERV + "testimoni/all";
    public static String URL_KREDITBCA = URL_SERV + "kreditbca/show";
    public static String URL_INVESTASI_INVESTOR = URL_SERV + "investasi/investor";
    public static String URL_INVESTASI_INVESTOR_FILE = URL_SERV + "investasi/uploadfile";
    public static String URL_INVESTASI_INVESTOR_FIND = URL_SERV + "investasi/findinvestor";
    public static String URL_INVESTASI_INVESTOR_DET = URL_SERV + "investasi/investordet";
    public static String URL_INVESTASI_INVESTOR_VERIF = URL_SERV + "investasi/verifikasi";
    public static String URL_INVESTASI_INVESTOR_INVALID = URL_SERV + "investasi/invalid";
    public static String URL_INVESTASI_INVESTOR_NOTES = URL_SERV + "investasi/investornote";
    public static String URL_INVESTASI_SELESAI = URL_SERV + "investasi/selesai";
    public static String URL_INVESTASI_VERIF = URL_SERV + "investasi/verif";
    public static String URL_INVESTASI_INVEST = URL_SERV + "investasi/invest";
    public static String URL_INVESTASI_INVEST2 = URL_SERV + "investasi/invest2";
    public static String URL_INVESTASI_INVEST_DET = URL_SERV + "investasi/investdet";
    public static String URL_INVESTASI_INVEST_FIND = URL_SERV + "investasi/findinvest";
    public static String URL_INVESTASI_INVEST_FIND2 = URL_SERV + "investasi/findinvest2";
    public static String URL_INVESTASI_INVEST1 = URL_SERV + "investasi/invest1";
    public static String URL_INVESTASI_INVESTARIK_APPROVE = URL_SERV + "investasi/approve";
    public static String URL_INVESTASI_INVESTARIK_APPROVE1 = URL_SERV + "investasi/approve1";
    public static String URL_INVESTASI_INVESTARIK_DISAPPROVE = URL_SERV + "investasi/disapprove";
    public static String URL_INVESTASI_INVESTARIK = URL_SERV + "investasi/investarik";
    public static String URL_INVESTASI_INVESTARIK1 = URL_SERV + "investasi/investarik1";
    public static String URL_INVESTASI_INVESTLAIN = URL_SERV + "investasi/investlain";
    public static String URL_INVESTASI_INVESTLAINFIND = URL_SERV + "investasi/findinvestlain";
    public static String URL_INVESTASI_INVESTARIK_FIND = URL_SERV + "investasi/findinvestarik";
    public static String URL_INVESTASI_INVESTARIK_FIND1 = URL_SERV + "investasi/findinvestarik1";
    public static String URL_DEBITBCA = URL_SERV + "debitbca/show";
    public static String URL_DEBITBCALIST = URL_SERV + "debitbca/list";
    public static String URL_KREDITBCA_MATCH = URL_SERV + "kreditbca/match";
    public static String URL_DEBITBCA_MATCH = URL_SERV + "debitbca/match";
    public static String URL_KREDITBCA_FINALIZE = URL_SERV + "kreditbca/finalize";
    public static String URL_KREDITBCA_UPLOAD = URL_SERV + "kreditbca/upload";
    public static String URL_DEBITBCA_FINALIZE = URL_SERV + "debitbca/finalize";
    public static String URL_DEBITBCA_SAVEUPLOAD = URL_SERV + "debitbca/saveupload";
    public static String URL_DEBITBCA_UPLOAD = URL_SERV + "debitbca/upload";
    public static String URL_TESTIMONI_SHOW = URL_SERV + "testimoni/show";
    public static String URL_ARTICLE_REMOVE = URL_SERV + "article/remove";
    public static String URL_ARTICLE_STORE = URL_SERV + "article/store";
    public static String URL_ARTICLE_UPDATE = URL_SERV + "article/update";
    public static String URL_KLIEN_STORE = URL_SERV + "klien/store";
    public static String URL_SMS_ALL = URL_SERV + "komunikasi/allsms";
    public static String URL_SMS_FIND = URL_SERV + "komunikasi/findsms";
    public static String URL_EMAIL_ALL = URL_SERV + "komunikasi/allemail";
    public static String URL_EMAIL_FIND = URL_SERV + "komunikasi/findemail";
    public static String URL_KLIEN_FIND = URL_SERV + "klien/find";
    public static String URL_KLIEN_UPDATE = URL_SERV + "klien/update";
    public static String URL_KLIEN_ACTIVATE = URL_SERV + "klien/activate";
    public static String URL_KLIEN_ACTIVATE1 = URL_SERV + "klien/resendactivation";
    public static String URL_PENGAJUAN = URL_SERV + "pengajuan/view_pengajuan";
    public static String URL_TASK_ASSIGNED_TO = URL_SERV + "task/showbyassignedto";
    public static String URL_TASK_ALL = URL_SERV + "task/all";
    public static String URL_TASK_REMOVE = URL_SERV + "task/remove";
    public static String URL_TASK_REMOVES = URL_SERV + "task/removes";
    public static String URL_TASK_SELESAIS = URL_SERV + "task/selesais";
    public static String URL_TASK_FIND = URL_SERV + "task/find";
    public static String URL_TASK_ANALISA = URL_SERV + "task/analisa";
    public static String URL_TASK_JATUHTEMPO = URL_SERV + "task/jatuhtempo";
    public static String URL_TASK_ANALISA_FIND = URL_SERV + "task/findanalisa";
    public static String URL_TASK_JATUHTEMPO_FIND = URL_SERV + "task/findjatuhtempo";
    public static String URL_TASK_VERIFIKASI = URL_SERV + "task/verifikasi";
    public static String URL_TASK_VERIFIKASI_FIND = URL_SERV + "task/findverifikasi";
    public static String URL_TASK_COLLECTIONS = URL_SERV + "task/collections";
    public static String URL_TASK_COLLECTIONS_FIND = URL_SERV + "task/findcollections";
    public static String URL_TASK_OTHERS = URL_SERV + "task/others";
    public static String URL_TASK_OTHERS_FIND = URL_SERV + "task/findothers";
    public static String URL_TASK_FINISH = URL_SERV + "task/selesai";
    public static String URL_TASK_STORE = URL_SERV + "task/store";
    public static String URL_KOMUNIKASI_EMAIL_BY_CLIENT = URL_SERV + "komunikasi/emailbyclient";
    public static String URL_KOMUNIKASI_EMAIL = URL_SERV + "komunikasi/email";
    public static String URL_KOMUNIKASI_EMAIL1 = URL_SERV + "komunikasi/email1";
    public static String URL_KOMUNIKASI_SMS = URL_SERV + "komunikasi/sms";
    public static String URL_KOMUNIKASI_SMS1 = URL_SERV + "komunikasi/sms1";
    public static String URL_KOMUNIKASI_SMS_BY_CLIENT = URL_SERV + "komunikasi/smsbyclient";
    public static String URL_PENGAJUAN_ALL = URL_SERV + "pengajuan/all";
    public static String URL_PENGAJUAN_STORE = URL_SERV + "pengajuan/store";
    public static String URL_PENGAJUAN_BATAL = URL_SERV + "pengajuan/batal";
    public static String URL_PENGAJUAN_JANJI_BAYAR = URL_SERV + "pengajuan/janjibayar";
    public static String URL_PENGAJUAN_JANJI_BAYAR1 = URL_SERV + "pengajuan/janjibayar1";
    public static String URL_PENGAJUAN_FIND_JANJI_BAYAR = URL_SERV + "pengajuan/findjanjibayar";
    public static String URL_PENGAJUAN_REQ_JANJI_BAYAR = URL_SERV + "pengajuan/reqjanjibayar";
    public static String URL_PENGAJUAN_REQ_JANJI_BAYAR1 = URL_SERV + "pengajuan/reqjanjibayar1";
    public static String URL_PENGAJUAN_FIND_REQ_JANJI_BAYAR = URL_SERV + "pengajuan/findreqjanjibayar";
    public static String URL_PENGAJUAN_LENYAP = URL_SERV + "pengajuan/lenyap";
    public static String URL_PENGAJUAN_LENYAP1 = URL_SERV + "pengajuan/lenyap1";
    public static String URL_PENGAJUAN_FIND_LENYAP = URL_SERV + "pengajuan/findlenyap";
    public static String URL_PENGAJUAN_CICILAN = URL_SERV + "pengajuan/cicilan";
    public static String URL_PENGAJUAN_CICILAN1 = URL_SERV + "pengajuan/cicilan1";
    public static String URL_PENGAJUAN_FIND_CICILAN = URL_SERV + "pengajuan/findcicilan";
    public static String URL_USER_ALL = URL_SERV + "user/all";
    public static String URL_USER_INFO = URL_SERV + "user/info";
    public static String URL_USER_PASS = URL_SERV + "user/passworduser";
    public static String URL_USER_PASS1 = URL_SERV + "user/passwordemail";
    public static String URL_PENGAJUAN_FIND = URL_SERV + "pengajuan/find";
    public static String URL_PENCAIRAN_ALL = URL_SERV + "pencairan/all";
    public static String URL_PENCAIRAN_ALL_DATA = URL_SERV + "pencairan/alldata";
    public static String URL_PENCAIRAN_FIND = URL_SERV + "pencairan/find";
    public static String URL_PERPANJANGAN_ALL = URL_SERV + "perpanjangan/all";
    public static String URL_PERPANJANGAN_ALL_DATA = URL_SERV + "perpanjangan/alldata";
    public static String URL_PERPANJANGAN_FIND = URL_SERV + "perpanjangan/find";
    public static String URL_PEMBAYARAN_ALL = URL_SERV + "pembayaran/all";
    public static String URL_PEMBAYARAN_FIND = URL_SERV + "pembayaran/find";
    public static String URL_DENDA_ALL = URL_SERV + "denda/all";
    public static String URL_DENDA_ALL_DATA = URL_SERV + "denda/alldata";
    public static String URL_DENDA_FIND = URL_SERV + "denda/find";
    public static String URL_PENGAJUAN_BARU = URL_SERV + "pengajuan/baru";
    public static String URL_PENGAJUAN_BARU_ = URL_SERV + "pengajuan/baru1";
    public static String URL_PENGAJUAN_BARU1 = URL_SERV + "pengajuan/barurepeat";
    public static String URL_PENGAJUAN_BARU1_ = URL_SERV + "pengajuan/barurepeat1";
    public static String URL_PENGAJUAN_BARU2 = URL_SERV + "pengajuan/barusetuju";
    public static String URL_PENGAJUAN_BARU2_ = URL_SERV + "pengajuan/barusetuju1";
    public static String URL_PENGAJUAN_BARU3 = URL_SERV + "pengajuan/barutolak";
    public static String URL_PENGAJUAN_BARU3_ = URL_SERV + "pengajuan/barutolak1";
    public static String URL_PENGAJUAN_FINDBARU = URL_SERV + "pengajuan/findbaru";
    public static String URL_PENGAJUAN_FINDBARU1 = URL_SERV + "pengajuan/findbarurepeat";
    public static String URL_PENGAJUAN_FINDBARU2 = URL_SERV + "pengajuan/findbarusetuju";
    public static String URL_PENGAJUAN_FINDBARU3 = URL_SERV + "pengajuan/findbarutolak";
    public static String URL_PENGAJUAN_AKTIF = URL_SERV + "pengajuan/aktif";
    public static String URL_PENGAJUAN_AKTIF1 = URL_SERV + "pengajuan/aktif1";
    public static String URL_DENDA_AKTIF = URL_SERV + "denda/aktif";
    public static String URL_PERPANJANGAN_AKTIF = URL_SERV + "perpanjangan/aktif";
    public static String URL_PEMBAYARAN_ALL_DATA = URL_SERV + "pembayaran/alldata";
    public static String URL_PEMBAYARAN_AKTIF = URL_SERV + "pembayaran/aktif";
    public static String URL_PENCAIRAN_AKTIF = URL_SERV + "pencairan/aktif";
    public static String URL_PENGAJUAN_FINDAKTIF = URL_SERV + "pengajuan/findaktif";
    public static String URL_PENGAJUAN_NON_AKTIF = URL_SERV + "pengajuan/nonaktif";
    public static String URL_PENGAJUAN_NON_AKTIF1 = URL_SERV + "pengajuan/nonaktif1";
    public static String URL_PERPANJANGAN_NON_AKTIF = URL_SERV + "perpanjangan/nonaktif";
    public static String URL_PEMBAYARAN_NON_AKTIF = URL_SERV + "pembayaran/nonaktif";
    public static String URL_PENCAIRAN_NON_AKTIF = URL_SERV + "pencairan/nonaktif";
    public static String URL_PENGAJUAN_FIND_NON_AKTIF = URL_SERV + "pengajuan/findnonaktif";
    public static String URL_PENGAJUAN_LUNAS = URL_SERV + "pengajuan/lunas";
    public static String URL_PENGAJUAN_LUNAS1 = URL_SERV + "pengajuan/lunas1";
    public static String URL_DENDA_LUNAS = URL_SERV + "denda/lunas";
    public static String URL_PERPANJANGAN_LUNAS = URL_SERV + "perpanjangan/lunas";
    public static String URL_PEMBAYARAN_LUNAS = URL_SERV + "pembayaran/lunas";
    public static String URL_PENCAIRAN_LUNAS = URL_SERV + "pencairan/lunas";
    public static String URL_PENGAJUAN_FINDLUNAS = URL_SERV + "pengajuan/findlunas";
    public static String URL_PENGAJUAN_JATUH_TEMPO = URL_SERV + "pengajuan/jatuhtempo";
    public static String URL_PENGAJUAN_JATUH_TEMPO1 = URL_SERV + "pengajuan/jatuhtempo1";
    public static String URL_DENDA_JATUH_TEMPO = URL_SERV + "denda/jatuhtempo";
    public static String URL_PERPANJANGAN_JATUH_TEMPO = URL_SERV + "perpanjangan/jatuhtempo";
    public static String URL_PEMBAYARAN_JATUH_TEMPO = URL_SERV + "pembayaran/jatuhtempo";
    public static String URL_PENCAIRAN_JATUH_TEMPO = URL_SERV + "pencairan/jatuhtempo";
    public static String URL_PENGAJUAN_FIND_JATUH_TEMPO = URL_SERV + "pengajuan/findjatuhtempo";
    public static String URL_PENGAJUAN_BAYAR_SEBAGIAN = URL_SERV + "pengajuan/bayarsebagian";
    public static String URL_PENGAJUAN_BAYAR_SEBAGIAN1 = URL_SERV + "pengajuan/bayarsebagian1";
    public static String URL_DENDA_BAYAR_SEBAGIAN = URL_SERV + "denda/bayarsebagian";
    public static String URL_PERPANJANGAN_BAYAR_SEBAGIAN = URL_SERV + "perpanjangan/bayarsebagian";
    public static String URL_PEMBAYARAN_BAYAR_SEBAGIAN = URL_SERV + "pembayaran/bayarsebagian";
    public static String URL_PENCAIRAN_BAYAR_SEBAGIAN = URL_SERV + "pencairan/bayarsebagian";
    public static String URL_PENGAJUAN_FIND_BAYAR_SEBAGIAN = URL_SERV + "pengajuan/findbayarsebagian";
    public static String URL_PENGAJUAN_COLLECTION = URL_SERV + "pengajuan/collection";
    public static String URL_PENGAJUAN_COLLECTION1 = URL_SERV + "pengajuan/collection1";
    public static String URL_DENDA_COLLECTION = URL_SERV + "denda/collection";
    public static String URL_PERPANJANGAN_COLLECTION = URL_SERV + "perpanjangan/collection";
    public static String URL_PEMBAYARAN_COLLECTION = URL_SERV + "pembayaran/collection";
    public static String URL_PENCAIRAN_COLLECTION = URL_SERV + "pencairan/collection";
    public static String URL_PENGAJUAN_FIND_COLLECTION = URL_SERV + "pengajuan/findcollection";
    public static String URL_PENGAJUAN_DEBT_COLLECTOR = URL_SERV + "pengajuan/debtcollector";
    public static String URL_PENGAJUAN_DEBT_COLLECTOR1 = URL_SERV + "pengajuan/debtcollector1";
    public static String URL_PENGAJUAN_BAD_DEBT = URL_SERV + "pengajuan/baddebt";
    public static String URL_PENGAJUAN_BAD_DEBT1 = URL_SERV + "pengajuan/baddebt1";
    public static String URL_DENDA_DEBT_COLLECTOR = URL_SERV + "denda/debtcollector";
    public static String URL_DENDA_BAD_DEBT = URL_SERV + "denda/baddebt";
    public static String URL_PERPANJANGAN_DEBT_COLLECTOR = URL_SERV + "perpanjangan/debtcollector";
    public static String URL_PERPANJANGAN_BAD_DEBT = URL_SERV + "perpanjangan/baddebt";
    public static String URL_PEMBAYARAN_DEBT_COLLECTOR = URL_SERV + "pembayaran/debtcollector";
    public static String URL_PEMBAYARAN_BAD_DEBT = URL_SERV + "pembayaran/baddebt";
    public static String URL_PENCAIRAN_DEBT_COLLECTOR = URL_SERV + "pencairan/debtcollector";
    public static String URL_PENCAIRAN_BAD_DEBT = URL_SERV + "pencairan/baddebt";
    public static String URL_PENGAJUAN_FIND_DEBT_COLLECTOR = URL_SERV + "pengajuan/finddebtcollector";
    public static String URL_PENGAJUAN_FIND_BAD_DEBT = URL_SERV + "pengajuan/findbaddebt";
    public static String URL_PENCAIRAN_SHOW_BY_PENGAJUAN = URL_SERV + "pencairan/showbypengajuan";
    public static String URL_PEMBAYARAN_SHOW_BY_PENGAJUAN = URL_SERV + "pembayaran/showbypengajuan";
    public static String URL_PERPANJANGAN_SHOW_BY_PENGAJUAN = URL_SERV + "perpanjangan/showbypengajuan";
    public static String URL_DENDA_SHOW_BY_PENGAJUAN = URL_SERV + "denda/showbypengajuan";
    public static String URL_REFERRAL_ALL = URL_SERV + "referral/all";
    public static String URL_REFERRAL_TRANSFER = URL_SERV + "referral/transfer";
    public static String URL_LOGACCOUNTING_ALL = URL_SERV + "logaccounting/all";
    public static String URL_LOGINTERNAL_ALL = URL_SERV + "loginternal/all";
    public static String URL_LOGSIMULASI_ALL = URL_SERV + "logsimulation/all";
    public static String URL_LOGACTIVITY_ALL = URL_SERV + "logactivity/all";
    public static String URL_LOGACTIVITY_REKAP = URL_SERV + "logactivity/rekap";
    public static String URL_LOGACTIVITY_DETAIL = URL_SERV + "logactivity/detail1";
    public static String URL_LOGOPERATOR_ALL = URL_SERV + "logoperator/all";

    public static String URL_PENCAIRAN_FINDALL = URL_SERV + "pencairan/findbaru";
    public static String URL_PENCAIRAN_FINDAKTIF = URL_SERV + "pencairan/findaktif";
    public static String URL_PENCAIRAN_FINDNONAKTIF = URL_SERV + "pencairan/findnonaktif";
    public static String URL_PENCAIRAN_FINDLUNAS = URL_SERV + "pencairan/findlunas";
    public static String URL_PENCAIRAN_FINDJATUHTEMPO = URL_SERV + "pencairan/findjatuhtempo";
    public static String URL_PENCAIRAN_FINDCOLLECTION = URL_SERV + "pencairan/findcollection";
    public static String URL_PENCAIRAN_FINDBAYARSEBAGIAN = URL_SERV + "pencairan/findbayarsebagian";
    public static String URL_PENCAIRAN_FINDDEBTCOLLECTOR = URL_SERV + "pencairan/finddebtcollector";
    public static String URL_PENCAIRAN_FINDBADDEBT = URL_SERV + "pencairan/findbaddebt";

    public static String URL_PEMBAYARAN_FINDAALL = URL_SERV + "pembayaran/findbaru";
    public static String URL_PEMBAYARAN_FINDAKTIF = URL_SERV + "pembayaran/findaktif";
    public static String URL_PEMBAYARAN_FINDNONAKTIF = URL_SERV + "pembayaran/findnonaktif";
    public static String URL_PEMBAYARAN_FINDLUNAS = URL_SERV + "pembayaran/findlunas";
    public static String URL_PEMBAYARAN_FINDJATUHTEMPO = URL_SERV + "pembayaran/findjatuhtempo";
    public static String URL_PEMBAYARAN_FINDCOLLECTION = URL_SERV + "pembayaran/findcollection";
    public static String URL_PEMBAYARAN_FINDBAYARSEBAGIAN = URL_SERV + "pembayaran/findbayarsebagian";
    public static String URL_PEMBAYARAN_FINDDEBTCOLLECTOR = URL_SERV + "pembayaran/finddebtcollector";
    public static String URL_PEMBAYARAN_FINDBADDEBT = URL_SERV + "pembayaran/findbaddebt";

    public static String URL_PERPANJANGAN_FINDALL = URL_SERV + "perpanjangan/findbaru";
    public static String URL_PERPANJANGAN_FINDAKTIF = URL_SERV + "perpanjangan/findaktif";
    public static String URL_PERPANJANGAN_FINDNONAKTIF = URL_SERV + "perpanjangan/findnonaktif";
    public static String URL_PERPANJANGAN_FINDLUNAS = URL_SERV + "perpanjangan/findlunas";
    public static String URL_PERPANJANGAN_FINDJATUHTEMPO = URL_SERV + "perpanjangan/findjatuhtempo";
    public static String URL_PERPANJANGAN_FINDCOLLECTION = URL_SERV + "perpanjangan/findcollection";
    public static String URL_PERPANJANGAN_FINDBAYARSEBAGIAN = URL_SERV + "perpanjangan/findbayarsebagian";
    public static String URL_PERPANJANGAN_FINDDEBTCOLLECTOR = URL_SERV + "perpanjangan/finddebtcollector";
    public static String URL_PERPANJANGAN_FINDBADDEBT = URL_SERV + "perpanjangan/findbaddebt";

    public static String URL_DENDA_FINDALL = URL_SERV + "denda/findbaru";
    public static String URL_DENDA_FINDAKTIF = URL_SERV + "denda/findaktif";
    public static String URL_DENDA_FINDNONAKTIF = URL_SERV + "denda/findnonaktif";
    public static String URL_DENDA_FINDLUNAS = URL_SERV + "denda/findlunas";
    public static String URL_DENDA_FINDJATUHTEMPO = URL_SERV + "denda/findjatuhtempo";
    public static String URL_DENDA_FINDCOLLECTION = URL_SERV + "denda/findcollection";
    public static String URL_DENDA_FINDBAYARSEBAGIAN = URL_SERV + "denda/findbayarsebagian";
    public static String URL_DENDA_FINDDEBTCOLLECTOR = URL_SERV + "denda/finddebtcollector";
    public static String URL_DENDA_FINDBADDEBT = URL_SERV + "denda/findbaddebt";

    public static String URL_PENGAJUAN_DETAIL = URL_SERV + "pengajuan/detail";
    public static String URL_PENGAJUAN_DETAILS = URL_SERV + "pengajuan/details";
    public static String URL_PENGAJUAN_NOTE = URL_SERV + "pengajuan/note";
    public static String URL_PENGAJUAN_NOTES_ALL = URL_SERV + "pengajuan/notesall";
    public static String URL_PENGAJUAN_HISTORI = URL_SERV + "pengajuan/historitahapan";
    public static String URL_PENGAJUAN_REKOMENDASI = URL_SERV + "pengajuan/rekomendasi";
    public static String URL_PENGAJUAN_REKOMENDASIS = URL_SERV + "pengajuan/rekomendasis";
    public static String URL_PENGAJUAN_TAHAP = URL_SERV + "pengajuan/tahap";
    public static String URL_PENGAJUAN_TAHAPS = URL_SERV + "pengajuan/tahaps";
    public static String URL_PENGAJUAN_DECISSION = URL_SERV + "pengajuan/decission";
    public static String URL_PENGAJUAN_DECISSIONS = URL_SERV + "pengajuan/decissions";
    public static String URL_PENGAJUAN_RENCANA_SURVEY = URL_SERV + "pengajuan/rencanasurvey";
    public static String URL_PENCAIRAN_CAIR = URL_SERV + "pencairan/cair";
    public static String URL_PENCAIRAN_CAIRS = URL_SERV + "pencairan/cairs";
    public static String URL_PEMBAYARAN_STORE = URL_SERV + "pembayaran/store";
    public static String URL_PEMBAYARAN_DELETE = URL_SERV + "pembayaran/remove";
    public static String URL_PERPANJANGAN_STORE = URL_SERV + "perpanjangan/store";
    public static String URL_PERPANJANGAN_DELETE = URL_SERV + "perpanjangan/remove";
    public static String URL_DENDA_STORE = URL_SERV + "denda/store";
    public static String URL_DENDA_DELETE = URL_SERV + "denda/remove";
    public static String URL_KLIEN_RATING = URL_SERV + "klien/rating";
    public static String URL_KLIEN_LIHAT = URL_SERV + "klien/lihat";
    public static String URL_KLIEN_MAXPINJAM = URL_SERV + "klien/maxpinjam";
    public static String URL_PENGAJUAN_SWITCHDENDA = URL_SERV + "pengajuan/switchdenda";
    public static String URL_PENGAJUAN_SWITCHDEBTCOLL = URL_SERV + "pengajuan/switchdebtcollector";
    public static String URL_PENGAJUAN_SWITCHCOLLECTION = URL_SERV + "pengajuan/switchcollection";
    public static String URL_PENGAJUAN_BADDEBT = URL_SERV + "pengajuan/baddebt";
    public static String URL_PENGAJUAN_BADDEBT1 = URL_SERV + "pengajuan/baddebt1";
    public static String URL_PENGAJUAN_BADDEBT2 = URL_SERV + "pengajuan/baddebt2";
    public static String URL_KLIEN_AUTOLINK = URL_SERV + "klien/links";
    public static String URL_PENGAJUAN_NEXTAUTO = URL_SERV + "pengajuan/nextauto";
    public static String URL_GET_API_BCA = BASE_URL_API_BCA + "apibca.php";
    public static String URL_GET_API_BCA_TRF = BASE_URL_API_BCA + "apibcatrf.php";
    public static String URL_JANJI_BAYAR = URL_SERV + "pengajuan/janjibayar";
    public static String URL_JANJI_BAYAR_FIND = URL_SERV + "pengajuan/findjanjibayar";
    public static String URL_REQ_JB= URL_SERV + "pengajuan/reqjb";
    public static String URL_INPUT_JB= URL_SERV + "pengajuan/inputjb";
    public static String URL_REQ_CICILAN= URL_SERV + "pengajuan/reqcicilan";
    public static String URL_SET_LENYAP= URL_SERV + "pengajuan/setlenyap";
    public static String URL_AKSI_JB= URL_SERV + "pengajuan/aksijb";


}
