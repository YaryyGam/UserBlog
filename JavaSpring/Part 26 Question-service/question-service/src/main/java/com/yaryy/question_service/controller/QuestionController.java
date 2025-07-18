package com.yaryy.question_service.controller;


import com.yaryy.question_service.model.Question;
import com.yaryy.question_service.model.QuestionWrapper;
import com.yaryy.question_service.model.Response;
import com.yaryy.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    Environment environment;

    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{topic}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable("topic") String category){
        return questionService.getQuestionsByCategory(category);
    }

    @PutMapping("/updateQuestion")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question){
        questionService.updateQuestion(question);
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @PostMapping("/addQuestion")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question){
        return questionService.addQuestion(question);
    }

    @DeleteMapping("/deleteQuestion/{questionId}")
    public String deleteQuestion(@PathVariable("questionId") int Id){
        questionService.deleteQuestion(Id);
        return "Deleted";
    }

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam Integer numQuestions){
        return questionService.getQuestionsForQuiz(category, numQuestions);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestions(@RequestBody List<Integer> questionIds){
        System.out.println(environment.getProperty("local.server.port"));
        return questionService.getQuestions(questionIds);
    }

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){
        return questionService.getScore(responses);
    }

    // create
    // getQuestion (id)
    // get Score

}
