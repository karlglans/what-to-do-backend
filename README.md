Ett utdrag ur project_info.pdf

token för exempel-användaren:
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc


###### Skapa ett todo
```
curl --location --request POST 'http://localhost:8080/api/v1/todos' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc' \
--header 'Content-Type: text/plain' \
--data-raw '{"message":"message 1","completed": true}'
```

###### Lista todo
```
curl --location --request GET 'http://localhost:8080/api/v1/todos' \
--header 'authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc'
```
###### Editera todo
Anropet kan se olika ut beroende på vad som skall ändras i en todo. Antingen skall {message} och/eller {completed} finnas med.
```
curl --location --request PATCH 'http://localhost:8080/api/v1/todos/1' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc' \
--header 'Content-Type: text/plain' \
--data-raw '{"completed": false}'
```
###### Ta bort  todo
```
curl --location --request DELETE 'http://localhost:8080/api/v1/todos/1' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc'
```
###### Ta bort  alla completed todos
```
curl --location --request DELETE 'http://localhost:8080/api/v1/todos/delete-completed' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc'
```
inte jättesnygg lösning men löser uppgiften. Skulle kanske ha använt en query parameter för att styra vad som skall deltas
###### Toggla completed
```
curl --location --request POST 'http://localhost:8080/api/v1/todos/toggle-complete' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJ3aGF0dG9kbyIsInN1YiI6MTAwLCJpYXQiOjE1ODc0NjQ0MDJ9.DJtkcZ656hMAYLCC7Ngxp4Edb9_YWwE1Q6n2_xw0_Zc'
```