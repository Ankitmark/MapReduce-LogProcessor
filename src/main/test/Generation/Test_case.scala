
package Generation

import Generation.RSGStateMachine.unit
import HelperUtils.Parameters
import com.mifmif.common.regex.Generex
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.PrivateMethodTester
import language.deprecated.symbolLiterals
import org.scalatest.matchers.should.Matchers

class Test_case extends AnyFlatSpec with Matchers with PrivateMethodTester {
  behavior of "Testing "


  val INITSTRING = "Starting the string generation"
  val init = unit(INITSTRING)
  val rsg = RandomStringGenerator((minStringLength, maxStringLength), randomSeed)

  private val maxStringLength = 10
  private val randomSeed = 1
  private val minStringLength = 1



  it should "generate a random string whose length is greater than the min length" in {
    val generationStep = init(rsg)
    generationStep._2.length shouldBe >= (minStringLength)
  }

  it should "generate two different random strings consecutively" in {
    val generationStep = init(rsg)
    val string1 = generationStep._2
    val string2 = generationStep._1.next._2
    string1 should not be string2
  }

  it should "locate an instance of the pattern in the generated string" in {
    val patternString = "([a-c][e-g][0-3]|[A-Z][5-9][f-w]){5,15}"
    val generex: Generex = new Generex(patternString)
    val generationStep = init(rsg)
    val string1 = generationStep._2
    val string2 = generationStep._1.next._2
    val genString = string1 + generex.random() + string2
    genString should include regex patternString.r
  }

  it should "return the same input string if the constructed random string is zero length" in {
    val someString = "someString"
    val rsg = RandomStringGenerator((1,2), 1)
    val callConstruct = PrivateMethod[String]('constructString)
    val result:String = rsg invokePrivate callConstruct(someString, 0)
    result should fullyMatch regex someString
  }

  it should "return a random string whose length is greater or equal to the one of the base string" in {
    val someString = "someString"
    val rsg = RandomStringGenerator((1,10), 1)
    val callConstruct = PrivateMethod[String]('constructString)
    val result:String = rsg invokePrivate callConstruct(someString, 0)
    result.length shouldBe >= (someString.length)
  }

  it should "return a random string that starts with the base string" in {
    val someString = "someString"
    val rsg = RandomStringGenerator((1,10), 1)
    val callConstruct = PrivateMethod[String]('constructString)
    val result:String = rsg invokePrivate callConstruct(someString, 10)
    result startsWith(someString)
  }

}
