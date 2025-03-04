```code
@startuml

class Usuario {
    - id : int
    - nome: string
    - login : string
    - senha : string

    + entrar(senha : string) : void
    + cadastrar(nome : string, login : string, senha : string) : void
    + sair() : void
}

class Aluno {
    - MAXOBRIGATORIAS : int = 4
    - MAXOPTATIVAS : int = 2
    - curso : Curso

    + matricularEmDisciplina(disciplina : Disciplina) : void
    + cancelarMatricula(disciplina : Disciplina) : void
    + confirmarMatricula() : void
}

class Professor {
    - disciplinas : List<Disciplina>

    + alunosMatriculados(disciplina : Disciplina) : List<Aluno>
}

class Secretaria {

    + gerarCurriculo() : void
    + atualizarInformacoesDisciplina() : void
    + atualizarInformacoesProfessor() : void
    + atualizarInformacoesAluno() : void
}

class Disciplina {
    - MINIMOALUNOS : int = 3
    - LIMITEALUNOS : int = 60 
    - nome : string
    - creditos : int
    - custo : float
    - ehObrigatoria : bool
    - alunosMatriculados : List<Aluno>
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