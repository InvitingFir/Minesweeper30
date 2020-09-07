package com.bignerdranch.minesweeper30.dataBase;

public class MineDbSchema {

    public static final class RecordTable {
        public static final String NAME = "records";

        public static final class Cols {
            public static final String TIME = "time";
            public static final String DATE = "date";
            public static final String BOMBS = "bombs";
            public static final String FIELD = "field";
            public static final String IMAGE = "image";
        }
    }
}
