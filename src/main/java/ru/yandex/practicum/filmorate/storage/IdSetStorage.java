package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

/**
 * Описывает функционал хранилища, которое хранит набор идентификаторов (ключей) с привязкой к другому идентификатору
 * (ключу). Например, набор идентификаторов пользователей, поставивших лайки фильму с идентификатором id.
 */
public interface IdSetStorage extends Storage<Set<Long>, Long> {}
