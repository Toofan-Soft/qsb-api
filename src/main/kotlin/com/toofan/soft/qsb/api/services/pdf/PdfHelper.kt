package com.toofan.soft.qsb.api.services.pdf

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject
import com.itextpdf.layout.element.*
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment

internal object PdfHelper {
    fun createPageBorder(pdfDocument: PdfDocument) {
        val numberOfPages = pdfDocument.numberOfPages

        for (i in 1..numberOfPages) {
            val page = pdfDocument.getPage(i)
            val pageSize = page.pageSize
            val pdfCanvas = PdfCanvas(page)
            val padding = 5.0

            pdfCanvas.setStrokeColor(ColorConstants.BLACK)
                .setLineWidth(1f)
                .rectangle(
                    pageSize.left.toDouble() + (5.0 + padding),
                    pageSize.bottom.toDouble() + (5.0 + padding),
                    pageSize.width.toDouble() - (10.0 + padding*2),
                    pageSize.height.toDouble() - (10.0 + padding*2)
                )
                .stroke()

            pdfCanvas.setStrokeColor(ColorConstants.BLACK)
                .setLineWidth(2f)
                .rectangle(
                    pageSize.left.toDouble() + (7.5 + padding),
                    pageSize.bottom.toDouble() + (7.5 + padding),
                    pageSize.width.toDouble() - (15.0 + padding*2),
                    pageSize.height.toDouble() - (15.0 + padding*2)
                )
                .stroke()

            pdfCanvas.setStrokeColor(ColorConstants.BLACK)
                .setLineWidth(1f)
                .rectangle(
                    pageSize.left.toDouble() + (10.0 + padding),
                    pageSize.bottom.toDouble() + (10.0 + padding),
                    pageSize.width.toDouble() - (20.0 + padding*2),
                    pageSize.height.toDouble() - (20.0 + padding*2)
                )
                .stroke()
        }
    }

    fun createSolidLine(weight: Float = 1f): Div {
        return Div()
//            .setMarginLeft(14f)
//            .setMarginRight(14f)
            .add(
                LineSeparator(SolidLine(weight))
            )
    }

    fun createDoubleSolidLine(weight: Float = 1f): Div {
        return Div()
            .setMarginLeft(14f)
            .setMarginRight(14f)
            .add(
                LineSeparator(SolidLine(weight))
            )
            .add(
                LineSeparator(SolidLine(weight))
                    .setMarginTop(0.5f)
            )
    }

    fun createDashedLine(): Paragraph {
        return Paragraph(
            Text(
                185.toString("_", true)
            )
                .setFontSize(3f)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setFont(Font.getFont(Data.Language.ENGLISH))
        ).apply {
            setTextAlignment(TextAlignment.CENTER)
        }
    }

    fun createParagraph(content: String, fontSize: Float = 12f, bold: Boolean = false): Paragraph {
        return Paragraph(content.value)
            .setTextAlignment(TextAlignment.CENTER)
            .setFont(Font.getFont(content, bold))
            .setFontSize(fontSize)
    }

    fun createCell(width: Float? = null, height: Float? = null): Cell {
        return Cell()
            .setBorder(null)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
            .apply {
                width?.let {
                    setWidth(it)
                }
                height?.let {
                    setHeight(it)
                }
            }
    }

    fun createTable(cells: List<Cell>, language: Data.Language): Table {
        return Table(cells.size)
            .setMarginLeft(12f)
            .setMarginRight(12f)
            .apply {
                when (language) {
                    Data.Language.ENGLISH -> cells
                    Data.Language.ARABIC -> cells.reversed()
                }.also {
                    for (cell in it) {
                        addCell(cell)
                    }
                }
            }
    }

    fun createImage(url: String?, width: Float? = null, height: Float? = null): Image? {
        return url?.let {
            it.url?.let {
//                Image(ImageDataFactory.create("E:\\f\\ToofanSoft\\QsB\\coding\\helper\\pdf-helper\\src\\main\\kotlin/res/logo.png"))
                Image(ImageDataFactory.create(url))
                    .setOpacity(0.5f)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .apply {
                        width?.let {
                            setWidth(it)
                        }
                        height?.let {
                            setHeight(it)
                        }
                    }
            }
        }
    }

    fun createCircleImage(
        pdfDocument: PdfDocument,
        fill: Boolean,
        letter: Char,
        language: Data.Language,
        radius: Double = 6.0
    ): Image {
        val r = radius.toFloat() * 2f

        // Create a PdfFormXObject (template) to draw the circle
        val circleTemplate = PdfFormXObject(PageSize(r + 2f, r + 2f))
        val canvas = PdfCanvas(circleTemplate, pdfDocument)

        // Draw the circle on the template
        canvas.setStrokeColor(ColorConstants.BLACK)
        canvas.setFillColor(if (fill) ColorConstants.LIGHT_GRAY else ColorConstants.WHITE)
        canvas.circle(radius + 1.0, radius + 1.0, radius)
        canvas.fillStroke()

        // Add the letter inside the circle
        val font = Font.getFont(language)
        val fontSize = 8f  // Adjust font size as needed
        canvas.beginText()
            .setFontAndSize(font, fontSize)
            .setFillColor(ColorConstants.BLACK) // Set text color to black

        // Calculate the width and height of the letter to center it
        val textWidth = font.getWidth(letter.toString(), fontSize)
//        val textWidth = 14f
        val textHeight = fontSize

        // Calculate the position to center the text
        val xPosition = radius + 1.0 - textWidth.toDouble() / 2.0
        val yPosition = (radius + 1.0 - textHeight.toDouble() / 4.0) - 0.5  // Adjust if necessary

        // Move to the calculated position and show the text
        canvas
            .moveText(xPosition, yPosition)
            .showText(letter.toString())
            .endText()

        val circleImage = Image(circleTemplate).apply {
            setWidth(r + 2f)
            setHeight(r + 2f)
        }

        return circleImage
    }
}