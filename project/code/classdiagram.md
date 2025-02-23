```code
@startuml

class Usuario {
    - id : int
    - login : string
    - senha : string

    + entrar(senha : String) : boolean
    + sair() : boolean
}

class Aluno {
    - nome : string
    - curso : Curso
    - disciplinasObrigatorias : List<Disciplina>
    - disciplinasOptativas : List<Disciplina>
    - maxObrigatorias : int = 4
    - maxOptativas : int = 2

    + matricularEmDisciplina(disciplina : Disciplina) : void
    + cancelarMatricula(disciplina : Disciplina) : void
    + confirmarMatricula() : void
}

class Professor {
    - nome : string
    - disciplinas : List<Disciplina>

    + alunosMatriculados(disciplina : Disciplina) : List<Aluno>
}

class Secretaria {
    - nome: string

    + gerarCurriculo() : void
    + atualizarInformacoesDisciplina() : void
    + atualizarInformacoesProfessor() : void
    + atualizarInformacoesAluno() : void
}

class Disciplina {
    - nome : string
    - creditos : int
    - ehObrigatoria : bool
    - alunosMatriculados : List<Aluno>
    - limiteAlunos : int = 60
    - minimoAlunos : int = 3
    - status : string

    + statusDisciplina() : string
    + gerarCurriculo() : void
    + fecharMatriculas() : void
    + adicionarAluno(aluno : Aluno) : void
    + removerAluno(aluno : Aluno) : void
    + alunosMatriculados() : List<Aluno>
    + cancelarDisciplina() : void
}

class Curso {
    - idCurso : int
    - nome : string
    - creditos : int
    - disciplinas : List<Disciplina>

    + adicionarDisciplina(disciplina : Disciplina) : void
}

class Cobranca {
    - idCobranca : int
    - aluno : Aluno
    - disciplinasCobradas : List<Disciplina>
    - valorTotal : float

    + gerarCobranca() : boolean
    + notificarCobranca() : void
    + calcularValorTotal() : float
}

Usuario <|-- Aluno
Usuario <|-- Professor
Usuario <|-- Secretaria

Aluno "1" -- "0..*" Curso 
Aluno "3..60" -- "1..*" Disciplina 
Professor "1" -- "0..*" Disciplina 
Curso "1" *-- "1..*" Disciplina 
Aluno "1" o-- "0..*" Cobranca 

@enduml
```
