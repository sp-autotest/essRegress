package dict;

import java.util.HashMap;

/**
 * Created by mycola on 28.08.2018.
 */
public class AddService {

    private static HashMap<String, String> map;

    public AddService() {
        map = new HashMap<>();
        map.put("0B5", "Предварительный выбор мест за дополнительную плату;" +
                "Prereserved seat assignment;" +
                "Erweiterte Platzauswahl gegen eine Zusatzgebühr;" +
                "Selección anticipada de asientos por un coste extra;" +
                "Selezione avanzata del posto, a una tariffa aggiuntiva;" +
                "Avancer le choix du fauteuil pour un coût supplémentaire;" +
                "预付费选位;" +
                "사전 좌석 선택 추가 수수료;" +
                "追加費用を支払って事前に座席を選択");
        map.put("0B3", "Закуска Сырная;" +
                "Appetizer Cheese;" +
                "Käsevorspeise;" +
                "Queso de aperitivo;" +
                "Antipasto a base di formaggio;" +
                "Amuse-gueule à base de fromage;" +
                "开胃奶酪;" +
                "애피타이저 치즈;" +
                "前菜 チーズ");
        map.put("019", "Десерт Шоколадная тарталетка;" +
                "Dessert Chocolate tartlet;" +
                "Dessert Schokoladentarte;" +
                "Tartaleta de chocolate de postre;" +
                "Dessert crostata al cioccolato;" +
                "En dessert, une tartelette au chocolat;" +
                "甜点巧克力挞;" +
                "디저트 초콜릿 타르틀렛;" +
                "デザート チョコレートタルト");
    }

    public static String getServiceByCodeAndLanguage(String code, int ln) {
        return map.get(code).split(";")[ln];
    }
}
