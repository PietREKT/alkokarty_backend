INSERT INTO card(rarity, id, content) VALUES (4, 1, 'Wybierasz gracza, który ma się napić');
INSERT INTO card(rarity, id, content) VALUES (3, 2, 'Pijesz');
INSERT INTO card(rarity, id, content) VALUES (2, 3, 'Ostatnia osoba, która klepnie się w udo pije');
INSERT INTO card(rarity, id, content) VALUES (3, 4, 'Piją dziewczyny');
INSERT INTO card(rarity, id, content) VALUES (3, 5, 'Piją chłopaki');
INSERT INTO card(rarity, id, content) VALUES (2, 6, 'Ostatnia osoba, która podniesie ręce w górę pije');
INSERT INTO card(rarity, id, content) VALUES (2, 7, 'Wybierasz gracza, który pije z tobą do końca gry lub dopóki nie wylosujesz tej karty kolejny raz');
INSERT INTO card(rarity, id, content) VALUES (4, 8, 'Freestyle. Wymyślasz linijkę i każdy kolejny gracz wymyśla kolejną, rymującą się. Kto nie da rady - pije');
INSERT INTO card(rarity, id, content) VALUES (4, 9, 'Gracze wymieniają przedmioty z wybranej przez ciebie kategorii; kto się powtórzy lub nie poda nowego przedmiotu, pije.');
INSERT INTO card(rarity, id, content) VALUES (4, 10, 'Nigdy przenigdy. Podajesz coś czego nigdy nie robiłeś, kto to robił musi się napić');
INSERT INTO card(rarity, id, content) VALUES (1, 11, 'Możesz zadawać pytania niezwiązane z grą, a odpowiadający pije, do momentu, gdy ktoś inny nie wylosuje tej karty.');
INSERT INTO card(rarity, id, content) VALUES (1, 12, 'Ustalasz regułę przed piciem obowiązującą całą grę. Kto się nie zastosuje - pije');
INSERT INTO card(rarity, id, content) VALUES (2, 13, 'Wszyscy piją');
INSERT INTO card(rarity, id, content) VALUES (1, 14, 'Wybierasz kto ma się napić shota z pieprzem');
INSERT INTO card(rarity, id, content) VALUES (1, 15, 'Wybierasz kto ma się napić shota z ketchupem');
INSERT INTO card(rarity, id, content) VALUES (3, 16, 'Zrób jaskółkę, jeśli ci się nie uda - musisz wypić.');
INSERT INTO card(rarity, id, content) VALUES (3, 17, 'Powiedz łamaniec językowy zadany przez innych uczestników. Jeśli ci się nie uda musisz się napić.');
INSERT INTO card(rarity, id, content) VALUES (1, 18, 'Opowiedz swoją najciekawszą historię po wypiciu dużej ilości alkoholu.');
INSERT INTO card(rarity, id, content) VALUES (3, 19, 'Wyjdź na zewnątrz i krzyknij "CHUJ" najgłośniej jak potrafisz, albo wypij');


insert into room(keep_alive, max_players, players_voted_count, playing, code, current_card, host_token, password)
    values (true, 20, 0, false, 'bajlandawka', null, null, '{bcrypt}$2a$10$uWjnUM9NIV8q/9cJUPxsbu6CBA4aUgvUxx7JPF06m4AY0hDFr8u02');
