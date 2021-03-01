package com.example.stockapp;

public class stockView {
        private String field;
        //private String value;

        public stockView(String field)  {
            this.field= field;
            //this.value =value;
        }

      //  public String getUrl() {
//            return value;
//        }
//
//        public void setUrl(String value) {
//            this.value = value;
//        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        @Override
        public String toString()  {
            return field;
        }

}
