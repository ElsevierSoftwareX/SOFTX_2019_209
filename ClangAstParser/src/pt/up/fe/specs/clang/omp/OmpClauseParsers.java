/**
 * Copyright 2017 SPeCS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.clang.omp;

import static pt.up.fe.specs.clava.ast.omp.clauses.OmpClauseKind.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.clava.ClavaLog;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpClauseKind;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpListClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpNumThreadsClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpProcBindClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpProcBindClause.ProcBindKind;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpReductionClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpReductionClause.ReductionKind;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpScheduleClause;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpScheduleClause.ScheduleKind;
import pt.up.fe.specs.clava.ast.omp.clauses.OmpScheduleClause.ScheduleModifier;
import pt.up.fe.specs.util.stringparser.ParserResult;
import pt.up.fe.specs.util.stringparser.StringParser;
import pt.up.fe.specs.util.stringparser.StringParsers;
import pt.up.fe.specs.util.utilities.StringSlice;

public class OmpClauseParsers {

    private static final Map<OmpClauseKind, Function<StringParser, OmpClause>> OMP_CLAUSE_PARSERS;
    static {
        OMP_CLAUSE_PARSERS = new HashMap<>();
        OMP_CLAUSE_PARSERS.put(SCHEDULE, OmpClauseParsers::parseSchedule);
        OMP_CLAUSE_PARSERS.put(REDUCTION, OmpClauseParsers::parseReduction);
        OMP_CLAUSE_PARSERS.put(NUM_THREADS, OmpClauseParsers::parseNumThreads);
        OMP_CLAUSE_PARSERS.put(PROC_BIND, OmpClauseParsers::parseProcBind);

        OMP_CLAUSE_PARSERS.put(PRIVATE, parser -> OmpClauseParsers.parseListClause(parser, PRIVATE));
        OMP_CLAUSE_PARSERS.put(SHARED, parser -> OmpClauseParsers.parseListClause(parser, SHARED));
    }

    public static Optional<Map<OmpClauseKind, OmpClause>> parse(StringParser pragmaParser) {
        Map<OmpClauseKind, OmpClause> clauses = new LinkedHashMap<>();

        // Apply rules while there are clauses
        while (!pragmaParser.isEmpty()) {

            Optional<OmpClause> parsedClause = pragmaParser.apply(OmpClauseParsers::parseOmpClause);

            parsedClause.ifPresent(clause -> clauses.put(clause.getKind(), clause));
            /*
            // Identify kind of clause
            OmpClauseKind clauseKind = getClauseKind(pragmaParser.getCurrentString());
            
            Function<StringParser, OmpClause> clauseParser = OMP_CLAUSE_PARSERS.get(clauseKind);
            
            if (clauseParser == null) {
                ClavaLog.info("Clause not implemented yet: " + clauseKind.getString());
                return Optional.empty();
            }
            // Preconditions.checkNotNull(clauseParser, "Clause not implemented yet: " + clauseKind);
            
            // Remove unused spaces
            // without this, the next call will fail to match any OmpClauseKind when parseClauseName is called
            // we can also hide this trim call in parseClauseName, but we need to make sure every parsing function will
            // call parseClauseName
            // pragmaParser.trim();
            
            OmpClause clause = clauseParser.apply(pragmaParser);
            
            clauses.put(clauseKind, clause);
            
            // Remove unused spaces
            pragmaParser.trim();
            
            // If starts with ',' remove
            pragmaParser.apply(StringParsers::checkCharacter, ',');
            */
        }

        return Optional.of(clauses);
    }

    private static ParserResult<Optional<OmpClause>> parseOmpClause(StringSlice string) {
        // Identify kind of clause
        OmpClauseKind clauseKind = getClauseKind(string);

        Function<StringParser, OmpClause> clauseParser = OMP_CLAUSE_PARSERS.get(clauseKind);

        if (clauseParser == null) {
            ClavaLog.info("Clause not implemented yet: " + clauseKind.getString());
            return new ParserResult<>(string, Optional.empty());
        }
        // Preconditions.checkNotNull(clauseParser, "Clause not implemented yet: " + clauseKind);

        // Remove unused spaces
        // without this, the next call will fail to match any OmpClauseKind when parseClauseName is called
        // we can also hide this trim call in parseClauseName, but we need to make sure every parsing function will
        // call parseClauseName
        // pragmaParser.trim();

        StringParser pragmaParser = new StringParser(string);
        // OmpClause clause = pragmaParser.applyFunction(clauseParser);
        OmpClause clause = clauseParser.apply(pragmaParser);

        // clauses.put(clauseKind, clause);

        // Remove unused spaces
        pragmaParser.trim();

        // If starts with ',' remove
        pragmaParser.apply(StringParsers::checkCharacter, ',');

        return new ParserResult<Optional<OmpClause>>(pragmaParser.getCurrentString(), Optional.of(clause));
    }

    private static OmpClauseKind getClauseKind(StringSlice currentPragma) {
        StringParser currentPragmaParser = new StringParser(currentPragma);

        // Remove unused spaces
        // without this, the next call will return an empty string if there are spaces between clauses
        // currentPragmaParser.trim();

        // Get up to the first space
        String currentPragmaPrefix = currentPragmaParser.apply(StringParsers::parseWord);

        // Check if clause has parenthesis
        int indexOfPar = currentPragmaPrefix.indexOf('(');

        String clauseName = indexOfPar == -1 ? currentPragmaPrefix : currentPragmaPrefix.substring(0, indexOfPar);

        return OmpClauseKind.getHelper().valueOf(clauseName.toLowerCase().trim());
    }

    private static StringParser parseClauseName(OmpClauseKind clauseKind, StringParser clauses) {
        int closeParIndex = clauses.getCurrentString().indexOf(')');
        Preconditions.checkArgument(closeParIndex != -1);

        String scheduleClauseString = clauses.substring(closeParIndex + 1);
        StringParser clause = new StringParser(scheduleClauseString);

        clause.apply(StringParsers::checkStringStarts, clauseKind.getString());
        clause.trim();
        clause.apply(StringParsers::checkStringStarts, "(");
        clause.apply(StringParsers::checkStringEnds, ")");

        return clause;
    }

    /**
     * Expects code in the form 'schedule([modifier [, modifier]:]kind[, chunk_size])'
     *
     * @param clause
     * @return
     */
    public static OmpScheduleClause parseSchedule(StringParser clauses) {
        /*
        int closeParIndex = clauses.getCurrentString().indexOf(')');
        Preconditions.checkArgument(closeParIndex != -1);
        
        String scheduleClauseString = clauses.substring(closeParIndex + 1);
        StringParser clause = new StringParser(scheduleClauseString);
        
        clause.apply(ClangGenericParsers::checkStringStarts, "schedule(");
        clause.apply(ClangGenericParsers::checkStringEnds, ")");
        */
        StringParser clause = parseClauseName(OmpClauseKind.SCHEDULE, clauses);

        String args = clause.toString();
        int colonIndex = args.indexOf(':');
        List<String> modifiersStrings = colonIndex == -1 ? Collections.emptyList()
                : parseList(args.substring(0, colonIndex));

        List<ScheduleModifier> modifiers = ScheduleModifier.getHelper().valueOf(modifiersStrings);

        args = colonIndex == -1 ? args : args.substring(colonIndex + 1);

        int commaIndex = args.indexOf(',');

        String scheduleString = commaIndex == -1 ? args : args.substring(0, commaIndex);
        ScheduleKind schedule = ScheduleKind.getHelper().valueOf(scheduleString.trim());

        Integer chunkSize = commaIndex == -1 ? null : Integer.decode(args.substring(commaIndex + 1).trim());

        return new OmpScheduleClause(schedule, chunkSize, modifiers);
    }

    /**
     * Parses a comma-separated list.
     *
     * @param list
     * @return
     */
    public static List<String> parseList(String list) {
        String[] array = list.toString().split(",");
        return Arrays.stream(array)
                .map(variable -> variable.trim())
                .collect(Collectors.toList());
    }

    private static OmpClause parseListClause(StringParser clauses, OmpClauseKind kind) {

        StringParser clause = parseClauseName(kind, clauses);

        List<String> variables = parseList(clause.toString());

        return new OmpListClause(kind, variables);

    }

    private static OmpReductionClause parseReduction(StringParser clauses) {

        StringParser clause = parseClauseName(REDUCTION, clauses);

        String args = clause.toString();

        // kind of reduction
        int colonIndex = clause.toString().indexOf(':');

        if (colonIndex < 1) {
            throw new RuntimeException("Badly formed reduction clause: reduction(" + args + ")"); // need more
                                                                                                  // information
        }

        String kindString = args.substring(0, colonIndex).trim();
        ReductionKind reductionKind = ReductionKind.getHelper().valueOf(kindString);

        // variable list
        List<String> variables = parseList(args.substring(colonIndex + 1));

        return new OmpReductionClause(reductionKind, variables);
    }

    private static OmpNumThreadsClause parseNumThreads(StringParser clauses) {

        StringParser clause = parseClauseName(NUM_THREADS, clauses);

        String expression = clause.apply(StringParsers::parseWord);

        return new OmpNumThreadsClause(expression);
    }

    private static OmpProcBindClause parseProcBind(StringParser clauses) {

        StringParser clause = parseClauseName(PROC_BIND, clauses);

        String arg = clause.toString().trim();
        ProcBindKind kind = ProcBindKind.getHelper().valueOf(arg);

        return new OmpProcBindClause(kind);
    }
}
