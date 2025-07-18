package com.yaryy.quiz_service.service;

import com.yaryy.quiz_service.DAO.QuizDAO;
import com.yaryy.quiz_service.feign.QuizInterface;
import com.yaryy.quiz_service.model.QuestionWrapper;
import com.yaryy.quiz_service.model.Quiz;
import com.yaryy.quiz_service.model.Response;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDAO quizDAO;

    @Autowired
    QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionsIds(questions);
        quizDAO.save(quiz);

        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Integer id) {
          Quiz quiz = quizDAO.findById(id)//.get();
                  .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Quiz з id " + id + " не знайдено"));;
          List<Integer> questionIds = quiz.getQuestionsIds();
          ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

          return questions;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizInterface.getScore(responses);

        return score;
    }
}
