'use client';

import { FormularioCadastro } from '@/components/operador/formulario-cadastro';
import { GridExtrato } from '@/components/operador/grid-extrato';

export default function OperadorPage() {
  return (
    <main className="container mx-auto p-6 space-y-8 max-w-7xl">
      <div className="border-b border-slate-200 dark:border-slate-800 pb-4">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-slate-50">
          Motor de Recebíveis & Câmbio
        </h1>
        <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
          Operações estruturadas de Duplicatas e Cheques com liquidação atômica e proteção contra Race Conditions.
        </p>
      </div>

      <section className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 dark:text-slate-100 mb-4">Novo Recebível / Simulação</h2>
        <FormularioCadastro />
      </section>

      <section className="bg-white dark:bg-slate-900 rounded-xl border border-slate-200 dark:border-slate-800 shadow-sm p-6">
        <h2 className="text-xl font-semibold text-slate-800 dark:text-slate-100 mb-4">Extrato Analítico de Transações</h2>
        <GridExtrato />
      </section>
    </main>
  );
}
