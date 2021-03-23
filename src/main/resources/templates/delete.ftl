package com.sergiuoltean.drools;

import java.lang.Number;

rule "Delete_${idx}"
	dialect "mvel"
	salience 18
	when
      del : ImportProduct( id == "${productId?c}")
	then
    retract( del );
end