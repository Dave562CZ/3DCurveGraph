package cz.richter.david.pgrf.curve3d.model

import com.sun.javaws.exceptions.InvalidArgumentException
import transforms3D.Kubika
import transforms3D.Point3D
import java.awt.Color
import java.util.ArrayList
import kotlin.properties.Delegates

/**
 * @author D.Richter
 * @since 16.5.2015
 */
public interface Curve {
    public fun getLines(): List<Triple<Point3D, Point3D, Color>>

    public fun get(index: Int): String

    public fun getCurveColor(): Color
}

abstract class AbstractCurve(val cubics: Kubika) : Curve {
    protected val immutableListOfLines: List<Triple<Point3D, Point3D, Color>> by Delegates.lazy {
        return@lazy computeLines()
    }
    abstract protected fun computeLines(): List<Triple<Point3D, Point3D, Color>>

    override public fun getLines(): List<Triple<Point3D, Point3D, Color>> {
        return immutableListOfLines
    }

    protected fun computeLines(begin: Point3D, numberOfLines: Int, color: Color): List<Triple<Point3D, Point3D, Color>> {
        val list: MutableList<Triple<Point3D, Point3D, Color>> = ArrayList()
        val isCoonsCubics = cubics == Cubics.coons

        var puv = begin
        for (i in 1..numberOfLines) {
            val t = i.toFloat() / numberOfLines
            val nas = cubics.compute(t.toDouble())
            if (!isCoonsCubics || i != 1) {
                list.add(Triple(puv, nas, color))
            }
            puv = nas
        }

        return ArrayList(list)
    }

}


public class BezierCurve(val begin: Point3D = Point3D(),
                         val vectorBegin: Point3D = Point3D(),
                         val end: Point3D = Point3D(),
                         val vectorEnd: Point3D = Point3D(),
                         val color: Color = Color.WHITE,
                         val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.bezier) {

    override fun getCurveColor(): Color = color

    override fun get(index: Int): String {
        return when (index) {
            0 -> "Bezier cubics"
            1 -> begin.getCoordsString() + end.getCoordsString()
            2 -> vectorBegin.getCoordsString() + vectorEnd.getCoordsString()
            else -> throw InvalidArgumentException(arrayOf("Index can be only in range 0 - 2"))
        }
    }

    override protected final fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        cubics.init(begin, vectorBegin, vectorEnd, end)
        return computeLines(begin = begin, numberOfLines = numberOfLines, color = color)
    }
}

public class FergusonCurve(val begin: Point3D = Point3D(),
                           val vectorBegin: Point3D = Point3D(),
                           val end: Point3D = Point3D(),
                           val vectorEnd: Point3D = Point3D(),
                           val color: Color = Color.WHITE,
                           val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.ferguson) {

    override fun getCurveColor(): Color = color

    override fun get(index: Int): String {
        return when (index) {
            0 -> "Ferguson cubics"
            1 -> begin.getCoordsString() + end.getCoordsString()
            2 -> vectorBegin.getCoordsString() + vectorEnd.getCoordsString()
            else -> throw InvalidArgumentException(arrayOf("Index can be only in range 0 - 2"))
        }
    }

    override protected fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        cubics.init(begin, end, vectorBegin, vectorEnd)
        return computeLines(begin = begin, numberOfLines = numberOfLines, color = color)
    }
}


public class CoonsCurve(val begin: Point3D = Point3D(),
                        val secondPoint: Point3D = Point3D(),
                        val thirdPoint: Point3D = Point3D(),
                        val end: Point3D = Point3D(),
                        val color: Color = Color.WHITE,
                        val numberOfLines: Int = 20)
                : AbstractCurve(Cubics.coons) {

    override fun getCurveColor(): Color = color

    override fun get(index: Int): String {
        return when (index) {
            0 -> "Coons cubics"
            1 -> begin.getCoordsString() + end.getCoordsString()
            2 -> secondPoint.getCoordsString() + thirdPoint.getCoordsString()
            else -> throw InvalidArgumentException(arrayOf("Index can be only in range 0 - 2"))
        }
    }

    override protected fun computeLines(): List<Triple<Point3D, Point3D, Color>> {
        cubics.init(begin, secondPoint, thirdPoint, end)
        return computeLines(begin = begin, numberOfLines = numberOfLines, color = color)
    }
}

object Cubics {
    val bezier = Kubika(0)
    val ferguson = Kubika(1)
    val coons = Kubika(2)
}

fun Point3D.getCoordsString(): String {
    return "[${x}; ${y}; ${z}]"
}

