```code
@startuml
left to right direction 
actor "Secretária da universidade" as secretaria 
actor "Aluno" as aluno 
actor "Professor" as professor 
actor "Usuário" as usuario

rectangle Matricula{ 
(Matricular em disciplina) -.> (Disciplina optativa) : extends
(Matricular em disciplina) -.> (Disciplina obrigatória) : include

usuario --> (Entrar no sistema)

(Entrar no sistema) .> (Cadastrar no sistema) : include

secretaria --> (Gerar curriculo) 
secretaria --> (Atualizar informações da disciplina)
secretaria --> (Atualizar informações dos professores)
secretaria --> (Atualizar informações dos alunos)

aluno --> (Cancelar matrícula) 
aluno --> (Matricular em disciplina) 
aluno --> (Confirmar matrícula)

(Confirmar matrícula) --> (Gerar cobrança) 
(Gerar cobrança) --> aluno

professor --> (Acessar alunos matriculados na disciplina)

usuario <|-- aluno 
usuario <|-- secretaria 
usuario <|-- professor 
}
@enduml

```
