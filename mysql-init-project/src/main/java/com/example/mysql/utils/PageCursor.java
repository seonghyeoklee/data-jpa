package com.example.mysql.utils;

import java.util.List;

public record PageCursor<T>(CursorRequest nextCursorRequest, List<T> body) {

}
