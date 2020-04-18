/**
 *
 */
package com.greenlee.core.helpmechoose.services;

import com.greenlee.helpmechoose.data.HelpMeChooseAnswerData;
import com.greenlee.helpmechoose.data.HelpMeChooseQuestionData;


/**
 * @author HelpMeChoose.santhanam
 * 
 */
public interface HelpMeChooseService
{
	public HelpMeChooseAnswerData getAnswer(String answerCode);

	public HelpMeChooseQuestionData getQuestion(String questionCode);
}
