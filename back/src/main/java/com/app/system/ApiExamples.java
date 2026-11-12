package com.app.system;

// Примеры ответов для Swagger (@ExampleObject). Тексты совпадают с сообщениями сервисов.
public final class ApiExamples {

    private ApiExamples() {
    }

    public static final String UNAUTHORIZED = """
            {"flag": false, "code": 401, "message": "Срок действия предоставленного токена доступа истек, он отозван, неправильно сформирован или недействителен по другим причинам", "data": "Invalid token"}""";

    public static final String FORBIDDEN = """
            {"flag": false, "code": 403, "message": "Нету доступа", "data": "Access Denied"}""";

    public static final String BOOK_NOT_FOUND = """
            {"flag": false, "code": 404, "message": "Не найдена книга по ИД: 999", "data": null}""";

    public static final String COPY_NOT_FOUND = """
            {"flag": false, "code": 404, "message": "Не найден экземпляр по ИД: 999", "data": null}""";

    public static final String ORDERING_NOT_FOUND = """
            {"flag": false, "code": 404, "message": "Не найдена заявка по ИД: 999", "data": null}""";

    // ---- ошибки индивидуального задания ----

    public static final String COPY_ISSUED = """
            {"flag": false, "code": 409, "message": "Экземпляр INV-0001-001 уже выдан", "data": null}""";

    public static final String COPY_IN_ISSUE = """
            {"flag": false, "code": 409, "message": "Экземпляр INV-0001-001 сейчас в выдаче", "data": null}""";

    public static final String READER_NOT_FOUND = """
            {"flag": false, "code": 404, "message": "Читатель не найден: 999", "data": null}""";

    public static final String UNPAID_FINE = """
            {"flag": false, "code": 409, "message": "У читателя неоплаченный штраф за просроченный возврат", "data": null}""";

    public static final String UNPAID_FINE_SELF = """
            {"flag": false, "code": 400, "message": "У вас есть неоплаченный штраф. Оформить новую заявку можно после его оплаты", "data": null}""";

    public static final String RETURN_ON_TIME = """
            {"flag": true, "code": 200, "message": "Success Returned",
             "data": {"id": 10, "status": "RETURNED", "statusName": "Возвращено", "date": "2026-11-12", "days": 14,
                      "dateEnd": "2026-11-12", "bookCopyId": 2, "inventoryNumber": "INV-0001-002",
                      "overdueDays": 0, "fineId": 0, "fineAmount": 0, "fineStatus": "", "fineStatusName": ""}}""";

    public static final String RETURN_OVERDUE = """
            {"flag": true, "code": 200, "message": "Возврат просрочен на 39 дн. Начислен штраф 581.10 BYN",
             "data": {"id": 3, "status": "RETURNED", "statusName": "Возвращено", "date": "2026-09-20", "days": 14,
                      "dateEnd": "2026-11-12", "bookCopyId": 4, "inventoryNumber": "INV-0002-001",
                      "overdueDays": 39, "fineId": 2, "fineAmount": 581.10, "fineStatus": "UNPAID", "fineStatusName": "Не оплачен"}}""";

    public static final String WRONG_STATUS = """
            {"flag": false, "code": 409, "message": "Действие недоступно для заявки в статусе «Возвращено»", "data": null}""";

    public static final String NO_FREE_COPIES = """
            {"flag": false, "code": 400, "message": "Нет свободных экземпляров книги «Война и мир»", "data": null}""";
}
