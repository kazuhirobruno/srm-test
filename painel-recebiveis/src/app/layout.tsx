import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'Motor de Recebíveis & Câmbio',
  description: 'Sistema transacional financeiro multimoedas',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="pt-BR">
      <body className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-slate-50 antialiased font-sans transition-colors duration-200">
        <div className="relative flex min-h-screen flex-col">
          <div className="flex-1">{children}</div>
        </div>
      </body>
    </html>
  );
}