package config;

import dict.AddService;
import dict.NationalityName;
import struct.Auto;
import struct.Hotel;
import struct.Price;
import dict.СityInfo;
import struct.ReportData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mycola on 20.02.2018.
 */
public class Values {

    public static СityInfo city = new СityInfo();
    public static AddService addService = new AddService();
    public static NationalityName nationalityName = new NationalityName();

    public static ReportData[] reportData = new ReportData[10];
    public static String currencyChange = "link"; //параметр определяет метод смены валюты.
                                                  //Возможные значения:
                                                  //    link - с помощью подмены параметров в линке
                                                  //    soap - с помощью soap-запросов
    public static String email = "tafl@software-provider.ru";
    public static String host = "https://afl-test.test.aeroflot.ru/sb/app/ru-";
    public static String city_table_host = "https://www.aeroflot.ru/ru-ru/flight/ways_map_table";
    public static String backdoor_host = "https://ws.ess.test.aeroflot.ru/test.php?PNR=";
    public static String office_host = "https://office.ess.test.aeroflot.ru/";
    public static String office_login = "";
    public static String office_password = "";


    public static String card[][] = {
        {"4154810047474554", "12", "2019", "314", "TEST TEST"}, //0 - Visa
        //{"2202000000000002", "12", "2019", "123", "TEST TEST"}, //1 - МИР
    };

    public static String lang[][] = {
            {"Russian", "русский", "ru", "ddMMMMyyyy,HH:mm—", "Россия"},
            {"English", "english", "en", "ddMMMyyyy,HH:mm—", "Russia"},
            {"German", "deutsch", "de", "ddMMMyyyy,HH:mm—", "Russland"},
            {"Spanish", "español", "es", "ddMMMyyyy,HH:mm—", "Rusia"},
            {"Italian", "italiano", "it", "ddMMMyyyy,HH:mm—", "Russia"},
            {"French", "le français", "fr", "ddMMMyyyy,HH:mm—", "Russie"},
            {"Chinese", "中文", "zh", "ddM月yyyy,HH:mm—", "俄罗斯"},
            {"Korean", "한국어", "ko", "ddMMMyyyy,HH:mm—", "러시아"},
            {"Japanese", "日本語", "ja", "ddMMMMyyyy,HH:mm—", "ロシア"},
    };

    public static String text[][] = {
            {"Полетная страховка", "Flight insurance", "Flugversicherung", "Seguro de vuelo", "Assicurazione sul volo", "Assurance vol", "飞行保险", "항공 보험", "フライト保険"},
            {"Медицинская страховка", "Medical insurance", "Krankenversicherung", "Seguro médico", "Assicurazione medica", "Assurance médicale", "医疗保险", "의료 보험", "医療保険"},
            {"ТРАНСПОРТ", "TRANSPORT", "Transport", "Transporte", "Trasporto", "Transport", "交通", "운송", "輸送"},
            {"Проживание", "Accommodation", "Unterkunft", "Alojamiento", "Alloggio", "Hébergement", "住宿", "숙박", "宿泊設備"},
            {"Спортивная", "Sports", "Sportarten", "Deportes", "Sport", "Sports", "体育运动", "스포츠", "スポーツ"},
            {"Добавить в заказ", "Add to your order", "Ihrer Bestellung hinzufügen", "Añadir a su pedido", "Aggiungi all`ordine", "Ajouter à votre commande", "添加到您的订单", "주문에 추가", "申込みする"},
            {"В заказе", "In the order", "In der Bestellung", "En el pedido", "Nell`ordine", "Dans la commande", "订单中", "주문", "注文内容"},
            {"АКПП", "Automatic transmission", "Automatikgetriebe", "Transmisión automática", "Cambio automatico", "Transmission automatique", "自动挡", "자동", "オートマチックトランスミッション"},
            {"Изменить выбранные опции", "Change selected options", "Ausgewählte Optionen ändern", "Cambiar las opciones seleccionadas", "Cambia opzioni selezionate", "Modifier les options sélectionnées", "更改选定选项", "선택한 옵션 변경", "選択したオプションを変更"},
            {"Изменить время или место", "Change time or place", "Zeit oder Ort ändern", "Cambiar hora o lugar", "Cambia ora o luogo", "Modifier l`heure ou l`endroit", "更改时间或地点", "시간/위치 변경", "時間または場所の変更"},
      /*10*/{"Изменить страховку", "Change insurance", "Versicherung ändern", "Cambiar seguro", "Cambia assicurazione", "Modifier l`assurance", "更改保险", "보험 변경", "保険の変更"},
            {"Оплата успешно завершена!", "Payment completed!", "Zahlung abgeschlossen!", "¡Pago completado!", "Pagamento completato!", "Paiement terminé !", "支付完成!", "결제 완료!", "お支払いが完了しました！"},
            {"Ваше бронирование успешно оплачено", "Your reservation is successfully paid", "Ihre Reservierung wurde erfolgreich bezahlt", "Su reserva se ha pagado correctamente", "La prenotazione è stata pagata correttamente", "Votre réservation a été payée avec succès", "您的预定已经付款成功", "예약에 대해 결제가 성공적으로 이루어졌습니다", "ご予約のお支払いは正常に行われました"},
            {"Электронная квитанция", "Electronic receipt", "Elektronischer Beleg", "Recibo electrónico", "Ricevuta elettronica", "Reçu électronique", "电子收据", "전자 확인증", "電子受領書"},
            {"Полис", "Policy", "Policen", "de póliza", "Polizza", "Politique", "保单号", "정책", "保険"},
            {"Ваучер", "Voucher", "Beleg", "de vale", "Voucher", "Bon", "优惠券编号", "우처", "バウチャー"},
            {"Билеты на аэроэкспресс", "Aeroexpress tickets", "Aeroexpress-Tickets", "Billetes Aeroexpress", "Biglietti Aeroexpress", "Billets Aeroexpress", "Aeroexpress 票", "Aeroexpress 항공권", "Aeroexpress 乗車券"},
            {"Бронирование отеля в %s", "Hotel reservations in %s", "Hotelreservierungen in %s", "Reservas de hotel en %s", "Prenotazioni in hotel a %s", "Réservations d`hôtel à %s", "在 %s 预订宾馆", "%s에서 호텔 예약", "%sでのホテルのご予約"},
            {"Белорусский вокзал", "Belorussky Rail Terminal", "Weißrussischen Bahnhof", "Terminal de ferrocarril Belorussky", "Terminal ferroviario Belorussky", "Terminus de la gare de Biélorussie", "白俄罗斯站", "벨로루스키역 터미널", "ベロルスキー鉄道ターミナル"},
            {"Шереметьево", "Sheremetyevo", "Sheremetyevo", "Sheremetiévo", "Sheremetyevo", "Chérémétiévo", "谢列梅捷沃", "셰레메티예보", "シェレメティエブ"},
      /*20*/{"Киевский вокзал", "Kievsky Rail Terminal", "Kiewer Bahnhof", "Terminal de ferrocarril Kievsky", "Terminal ferroviario Kievsky", "Terminus de la gare de Kiev", "基辅火车站", "키옙스키역 터미널", "キエフスキー鉄道ターミナル"},
            {"Внуково", "Vnukovo", "Vnukovo", "Vnukovo", "Vnukovo", "Vnoukovo", "尤努科沃", "브누코보", "ヴヌーコヴォ"},
            {"Бесплатно", "Free of charge", "Kostenlos", "Gratis", "Gratis", "Gratuit", "免费", "무료", "無料"},
            {"До", "Till", "Bis", "Hasta el", "Entro il:", "Jusqu`au", "截止", "까지", "まで"},
            {"Прокат автомобилей", "Car rental", "Autovermietung", "Alquiler de coches", "Noleggio auto", "Location de voiture", "租车", "렌터카", "レンタカー"},
            {"Бронирование трансфера", "Booking a transfer", "Transfer buchen", "Reservar un traslado", "Prenotare un trasferimento", "Réservation d'un transfert", "预订换乘", "트랜스퍼 예약", "乗り換えの予約"},
            {"Стандарт", "Standard class", "Standard", "Clase Standard", "Classe Standard", "Classe standard", "标准舱", "스탠다드 클래스", "スタンダードクラス"},
            {"Трансфер", "Transfer", "Transfer", "Traslado", "Trasferimento", "Transfert", "换乘", "트랜스퍼", "乗り換え"},
            {"Белорусский вокзал", "Belorussky Rail Terminal", "Weißrussischen Bahnhof", "Terminal de ferrocarril Belorussky", "Terminal ferroviario Belorussky", "Terminus de la gare de Biélorussie", "白俄罗斯火车站", "벨로루스키역 터미널", "ベロルスキー鉄道ターミナル"},
            {"Шереметьево", "Sheremetyevo", "Sheremetyevo", "Sheremetiévo", "Sheremetyevo", "Chérémétiévo", "谢列梅捷沃", "셰레메티예보", "シェレメーチエヴォ"},
      /*30*/{"Билет №", "Ticket No.", "Ticket-Nr.", "N.° de billete", "Biglietto n.", "Ticket N°", "票据编号", "항공권 번호", "チケット番号"}
    };

    public static String getPNR(int i) {
        return reportData[i].getPnr();
    }

    public static void setPNR(int i, String pnr) {
        reportData[i].setPnr(pnr);
    }

    public static String getDOC(int i) {
        return reportData[i].getDocs();
    }

    public static void setDOC(int i, String doc) {
        reportData[i].setDocs(doc);
    }

    public static List<String> getERR(int i) {
        return reportData[i].getErrors();
    }

    public static void addERR(int i, String error) {
        reportData[i].addError(error);
    }


}
