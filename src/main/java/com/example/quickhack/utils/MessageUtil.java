package com.example.quickhack.utils;

public class MessageUtil {
    public static final String SYSTEM_PROMPT = """
            I need to scan Kazakhstan document ID card, driver's license. In case of card ID - In front side(where we have human photo) under 'ТЕГІ/ФАМИЛИЯ' will be suname, 'АТЫ/ИМЯ' will be name, 'ӘКЕСІНҢ/ОТЕЧЕСТВО' will be middle name mark as middle_name, 'ТУҒАН КҮНІ/ДАТА РОЖДЕНИЯ' will be date in 'dd/MM/yyyy' format mark as birthday, and right to 'ЖСН/ИИН' will be 12 numbers - iin or id of person. Then in back side on the upper-right side 9 numbers, it will be id of card itself mark as card_id, and under 'БЕРІЛГЕН КҮНІ - ҚОЛДАНУ МЕРЗІМІ/ДАТА ВЫДАЧИ - СРОК ДЕЙСТВИЯ' will be two date in 'dd/MM/yyyy' format take it, mark first date as given_date, and second date as expiration_date. driver_license is still in development. I will give json request in following format {'doc_type': 'id_cart/driver_id', 'file_id': 'file_id from openai store'}, and will expect answer in json format: {'status': 'success/error', 'data': 'the data from pdf file'} in the case of ID card the 'data' will be following: {'iin' '12 numbers', 'full_number': 'surname name middle_name', 'birthday': 'dd/MM/yyyy', 'card_id': '9 numbers', 'given_date': 'dd/MM/yyyy', 'expiration_date': 'dd/MM/yyyy'}. I need just json response without any explanations and notes. Return success only in case of presence of all fields of document. In case of driver_license please skip it for now. And you can provide your comment in case of error in data in the field named 'message'.
           
            """;
}
