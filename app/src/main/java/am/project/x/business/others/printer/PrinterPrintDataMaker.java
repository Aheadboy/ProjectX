/*
 * Copyright (C) 2018 AlexMofer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package am.project.x.business.others.printer;

import android.content.Context;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import am.project.x.R;
import am.project.x.utils.QRCodeUtil;
import am.util.printer.PrintDataMaker;
import am.util.printer.PrinterWriter;
import am.util.printer.PrinterWriter58mm;
import am.util.printer.PrinterWriter80mm;

/**
 * 数据生成器
 * Created by Alex on 2016/11/10.
 */

class PrinterPrintDataMaker implements PrintDataMaker {

    private Context context;
    private String qr;
    private boolean enable;
    private int width;
    private int height;

    PrinterPrintDataMaker(Context context, String qr, boolean enable, int width, int height) {
        this.context = context;
        this.qr = qr;
        this.enable = enable;
        this.width = width;
        this.height = height;
        if (this.width <= 0 || this.height <= 0)
            this.enable = false;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();
        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ?
                    new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());

            if (enable) {
                ArrayList<byte[]> image1 = printer.getImageByte(context.getResources(),
                        R.drawable.ic_printer_logo);
                data.addAll(image1);
            }

            printer.setAlignLeft();
            printer.printLine();
            printer.printLineFeed();

            printer.printLineFeed();
            printer.setAlignCenter();
            printer.setEmphasizedOn();
            printer.setFontSize(1);
            printer.print("我的餐厅");
            printer.printLineFeed();
            printer.setFontSize(0);
            printer.setEmphasizedOff();
            printer.printLineFeed();

            printer.print("最时尚的明星餐厅");
            printer.printLineFeed();
            printer.print("客服电话：400-8008800");
            printer.printLineFeed();

            printer.setAlignLeft();
            printer.printLineFeed();

            printer.print("订单号：88888888888888888");
            printer.printLineFeed();

            printer.print("预计送达：" +
                    new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                            .format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();

            printer.setEmphasizedOn();
            printer.print("#8（已付款）");
            printer.printLineFeed();
            printer.print("××区××路×××大厦××楼×××室");
            printer.printLineFeed();
            printer.setEmphasizedOff();
            printer.print("13843211234");
            printer.print("（张某某）");
            printer.printLineFeed();
            printer.print("备注：多加点辣椒，多加点香菜，多加点酸萝卜，多送点一次性手套");
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();

            printer.printInOneLine("星级美食（豪华套餐）×1", "￥88.88", 0);
            printer.printLineFeed();
            printer.printInOneLine("星级美食（限量套餐）×1", "￥888.88", 0);
            printer.printLineFeed();
            printer.printInOneLine("餐具×1", "￥0.00", 0);
            printer.printLineFeed();
            printer.printInOneLine("配送费", "免费", 0);
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();

            printer.setAlignRight();
            printer.print("合计：977.76");
            printer.printLineFeed();
            printer.printLineFeed();

            printer.setAlignCenter();

            data.add(printer.getDataAndReset());

            if (enable) {
                File dir = context.getExternalFilesDir("Temp");
                if (dir == null) {
                    dir = context.getFilesDir();
                }
                String bitmapPath = dir.getPath() + "/tmp_qr.jpg";
                if (QRCodeUtil.createQRImage(qr, 380, 380, null,
                        bitmapPath)) {
                    ArrayList<byte[]> image2 = printer.getImageByte(bitmapPath);
                    data.addAll(image2);
                } else {
                    ArrayList<byte[]> image2 = printer
                            .getImageByte(context.getResources(), R.drawable.ic_printer_qr);
                    data.addAll(image2);
                }
            }

            printer.printLineFeed();
            printer.print("扫一扫，查看详情");
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();

            printer.feedPaperCutPartial();

            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
